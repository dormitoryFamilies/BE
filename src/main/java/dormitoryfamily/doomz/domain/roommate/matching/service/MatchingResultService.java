package dormitoryfamily.doomz.domain.roommate.matching.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;
import dormitoryfamily.doomz.domain.roommate.matching.event.result.MatchingResultEvent;
import dormitoryfamily.doomz.domain.roommate.matching.exception.AlreadyMatchedMemberException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingInterruptException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingLockException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingResultNotExistException;
import dormitoryfamily.doomz.domain.roommate.matching.exception.MemberDormitoryMismatchException;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingResultRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
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
    private final Map<Long, ReentrantLock> memberLocks = new ConcurrentHashMap<>();

    @Transactional
    public void saveMatchingResult(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member targetMember = getMemberById(memberId);

        // 두 회원에 대한 락 획득
        ReentrantLock lock1 = getLockForMember(loginMember.getId());
        ReentrantLock lock2 = getLockForMember(targetMember.getId());

        // 데드락 방지를 위해 ID 순서대로 락 획득
        ReentrantLock firstLock = loginMember.getId() < targetMember.getId() ? lock1 : lock2;
        ReentrantLock secondLock = loginMember.getId() < targetMember.getId() ? lock2 : lock1;

        boolean firstLockAcquired = false;
        boolean secondLockAcquired = false;

        try {
            // 5초 타임아웃으로 첫 번째 락 획득 시도
            firstLockAcquired = firstLock.tryLock(5, TimeUnit.SECONDS);
            if (!firstLockAcquired) {
                throw new MatchingLockException();
            }

            // 3초 타임아웃으로 두 번째 락 획득 시도
            secondLockAcquired = secondLock.tryLock(3, TimeUnit.SECONDS);
            if (!secondLockAcquired) {
                throw new MatchingLockException();
            }

            // 락 획득 후 매칭 로직 실행
            validateMatchingCapability(loginMember, targetMember);
            MatchingResult matchingResult = MatchingResult.createMatchingResult(loginMember, targetMember);
            matchingResultRepository.save(matchingResult);
            updateMemberMatchingStatus(loginMember, targetMember);
            matchingRequestService.deleteMatchingRequestWhenMatched(loginMember, targetMember);
            notifyMatchingResultInfo(matchingResult);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MatchingInterruptException();
        } finally {
            // 락 해제 (획득한 순서 반대로)
            if (secondLockAcquired) {
                secondLock.unlock();
            }
            if (firstLockAcquired) {
                firstLock.unlock();
            }
        }
    }

    private ReentrantLock getLockForMember(Long memberId) {
        return memberLocks.computeIfAbsent(memberId, k -> new ReentrantLock());
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
