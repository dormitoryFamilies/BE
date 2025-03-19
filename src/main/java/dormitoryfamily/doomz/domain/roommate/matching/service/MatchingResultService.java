package dormitoryfamily.doomz.domain.roommate.matching.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;
import dormitoryfamily.doomz.domain.roommate.matching.event.result.MatchingResultEvent;
import dormitoryfamily.doomz.domain.roommate.matching.exception.AlreadyMatchedMemberException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingResultNotExistException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MemberDormitoryMismatchException;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingResultRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.MATCHING_ACCEPT;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingResultService {

    private final MatchingResultRepository matchingResultRepository;
    private final MatchingRequestService matchingRequestService;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, ReentrantLock> memberLocks = new ConcurrentHashMap<>();

    @Transactional
    public void saveMatchingResult(PrincipalDetails principalDetails, Long memberId) {
        Pair<Member, Member> members = getOrderedMembersWithLock(memberId, principalDetails);
        Member loginMember = members.getFirst();
        Member targetMember = members.getSecond();

        validateMatchingCapability(loginMember, targetMember);
        MatchingResult matchingResult = MatchingResult.createMatchingResult(loginMember, targetMember);
        matchingResultRepository.save(matchingResult);
        updateMemberMatchingStatus(loginMember, targetMember);
        matchingRequestService.deleteMatchingRequestWhenMatched(loginMember, targetMember);
        notifyMatchingResultInfo(matchingResult);
    }

    public Pair<Member, Member> getOrderedMembersWithLock(Long memberId, PrincipalDetails principalDetails) {
        Long loginMemberId = principalDetails.getMember().getId();

        // ID 정렬 (데드락 방지)
        Long firstId = Math.min(loginMemberId, memberId);
        Long secondId = Math.max(loginMemberId, memberId);

        // 비관적 락으로 조회
        Member firstMember = memberRepository.findByIdWithPessimisticLock(firstId)
                .orElseThrow(MemberNotExistsException::new);

        Member secondMember = memberRepository.findByIdWithPessimisticLock(secondId)
                .orElseThrow(MemberNotExistsException::new);

        // 정확한 loginMember, targetMember 할당
        Member loginMember = (firstMember.getId().equals(loginMemberId)) ? firstMember : secondMember;
        Member targetMember = (firstMember.getId().equals(loginMemberId)) ? secondMember : firstMember;

        return Pair.of(loginMember, targetMember);
    }

    private void notifyMatchingResultInfo(MatchingResult matchingResult) {
        eventPublisher.publishEvent(new MatchingResultEvent(matchingResult, MATCHING_ACCEPT));
    }

    private void validateMatchingCapability(Member loginMember, Member targetMember){
        checkIfAlreadyMatchedMember(loginMember, targetMember);
        checkDormitoryMatch(loginMember, targetMember);
    }

    private void checkIfAlreadyMatchedMember(Member loginMember, Member targetMember) {
        if (targetMember.isRoommateMatched() || loginMember.isRoommateMatched()) {
            throw new AlreadyMatchedMemberException();
        }
    }

    private void checkDormitoryMatch(Member loginMember, Member targetMember) {
        if (!Objects.equals(loginMember.getDormitoryType(), targetMember.getDormitoryType())){
            throw new MemberDormitoryMismatchException();
        }
    }

    private void updateMemberMatchingStatus(Member loginMember, Member targetMember) {
        loginMember.markAsMatched();
        targetMember.markAsMatched();
    }

    public void cancelMatchingResult(PrincipalDetails principalDetails, Long memberId) {
        Pair<Member, Member> members = getOrderedMembersWithLock(memberId, principalDetails);
        Member loginMember = members.getFirst();
        Member targetMember = members.getSecond();

        MatchingResult matchingResult = getMatchingResultByMembers(loginMember, targetMember);
        matchingResultRepository.delete(matchingResult);

        resetMemberMatchingStatus(loginMember, targetMember);
    }

    private MatchingResult getMatchingResultByMembers(Member loginMember, Member targetMember) {
        return matchingResultRepository.findByMembers(loginMember, targetMember)
                .orElseThrow(MatchingResultNotExistException::new);
    }

    private void resetMemberMatchingStatus(Member loginMember, Member targetMember) {
        loginMember.markAsUnmatched();
        targetMember.markAsUnmatched();
    }
}
