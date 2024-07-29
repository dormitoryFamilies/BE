package dormitoryfamily.doomz.domain.matchingResult.service;

import dormitoryfamily.doomz.domain.matchingRequest.exception.*;
import dormitoryfamily.doomz.domain.matchingRequest.service.MatchingRequestService;
import dormitoryfamily.doomz.domain.matchingResult.entity.MatchingResult;
import dormitoryfamily.doomz.domain.matchingResult.exception.MatchingResultNotExistException;
import dormitoryfamily.doomz.domain.matchingResult.repository.MatchingResultRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingResultService {

    private final MatchingResultRepository matchingResultRepository;
    private final MatchingRequestService matchingRequestService;
    private final MemberRepository memberRepository;

    public void saveMatchingResult(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        validateMatchingResult(loginMember, targetMember);

        MatchingResult matchingResult = MatchingResult.createMatchingResult(loginMember, targetMember);
        matchingResultRepository.save(matchingResult);

        updateMemberMatchingStatus(loginMember, targetMember);
        matchingRequestService.deleteMatchingRequest(loginMember, targetMember);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void validateMatchingResult(Member loginMember, Member targetMember){
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
        Member loginMember = principalDetails.getMember();
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
