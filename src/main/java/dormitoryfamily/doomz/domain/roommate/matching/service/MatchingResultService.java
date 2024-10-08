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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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

    public void saveMatchingResult(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member targetMember = getMemberById(memberId);

        validateMatchingCapability(loginMember, targetMember);

        MatchingResult matchingResult = MatchingResult.createMatchingResult(loginMember, targetMember);
        matchingResultRepository.save(matchingResult);

        updateMemberMatchingStatus(loginMember, targetMember);
        matchingRequestService.deleteMatchingRequestWhenMatched(loginMember, targetMember);
        //알림 전송
        notifyMatchingResultInfo(matchingResult);
    }

    private void notifyMatchingResultInfo(MatchingResult matchingResult) {
        eventPublisher.publishEvent(new MatchingResultEvent(matchingResult, MATCHING_ACCEPT));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
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
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member targetMember = getMemberById(memberId);

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
