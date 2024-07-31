package dormitoryfamily.doomz.domain.matching.service;

import dormitoryfamily.doomz.domain.matching.exception.*;
import dormitoryfamily.doomz.domain.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.matching.repository.MatchingRequestRepository;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingRequestService {

    private final MatchingRequestRepository matchingRequestRepository;
    private final MemberRepository memberRepository;

    public void saveMatchingRequest(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        validateMatchingRequestCapability(loginMember, targetMember);

        MatchingRequest matchingRequest = MatchingRequest.createMatchingRequest(loginMember, targetMember);
        matchingRequestRepository.save(matchingRequest);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void validateMatchingRequestCapability(Member loginMember, Member targetMember) {
        checkIfAlreadyMatchedMember(loginMember, targetMember);
        checkDistinctMembers(loginMember, targetMember);
        checkDormitoryMatch(loginMember, targetMember);
        if(isMatchingRequestAlreadyExits(loginMember, targetMember)) {
            throw new MatchingRequestAlreadyExitsException();
        }
    }

    private void checkIfAlreadyMatchedMember(Member loginMember, Member targetMember) {
        if (targetMember.isRoommateMatched() || loginMember.isRoommateMatched()) {
            throw new AlreadyMatchedMemberException();
        }
    }

    private void checkDistinctMembers(Member loginMember, Member targetMember) {
        if (Objects.equals(loginMember.getId(), targetMember.getId())) {
            throw new CannotMatchingYourselfException();
        }
    }

    private void checkDormitoryMatch(Member loginMember, Member targetMember) {
        if (!Objects.equals(loginMember.getDormitoryType(), targetMember.getDormitoryType())) {
            throw new MemberDormitoryMismatchException();
        }
    }

    public boolean isMatchingRequestAlreadyExits(Member loginMember, Member targetMember) {
        return matchingRequestRepository.findByMembers(loginMember, targetMember).isPresent();

    }

    public void deleteMatchingRequest(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        MatchingRequest matchingRequest = getMatchingRequestByMembers(loginMember, targetMember);
        matchingRequestRepository.delete(matchingRequest);
    }

    public MatchingRequest getMatchingRequestByMembers(Member loginMember, Member targetMember) {
        return matchingRequestRepository.findByMembers(loginMember, targetMember)
                .orElseThrow(MatchingRequestNotExistException::new);
    }

    public void deleteMatchingRequestWhenMatched(Member loginMember, Member targetMember) {
        MatchingRequest matchingRequest = getMatchingSenderAndReceiver(loginMember, targetMember);
        matchingRequestRepository.delete(matchingRequest);
    }

    public MatchingRequest getMatchingSenderAndReceiver(Member loginMember, Member targetMember) {
        return matchingRequestRepository.findBySenderAndReceiver(targetMember, loginMember)
                .orElseThrow(MatchingRequestNotExistException::new);
    }
}
