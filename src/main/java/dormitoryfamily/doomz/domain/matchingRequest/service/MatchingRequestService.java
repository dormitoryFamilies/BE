package dormitoryfamily.doomz.domain.matchingRequest.service;

import dormitoryfamily.doomz.domain.matchingRequest.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.matchingRequest.exception.CannotMatchingYourselfException;
import dormitoryfamily.doomz.domain.matchingRequest.exception.MatchingRequestAlreadyExitsException;
import dormitoryfamily.doomz.domain.matchingRequest.exception.MatchingRequestNotExistException;
import dormitoryfamily.doomz.domain.matchingRequest.repository.MatchingRequestRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
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

    public void saveMatchingRequest(PrincipalDetails principalDetails, Long memberId){
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        validateMatchingRequest(loginMember, targetMember);

        MatchingRequest matchingRequest = MatchingRequest.createMatchingRequest(loginMember, targetMember);
        matchingRequestRepository.save(matchingRequest);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void validateMatchingRequest(Member loginMember, Member targetMember){
        checkDistinctMembers(loginMember, targetMember);
        checkIfAlreadyRequested(loginMember, targetMember);
    }

    private void checkDistinctMembers(Member loginMember, Member targetMember){
        if(Objects.equals(loginMember.getId(), targetMember.getId())){
            throw new CannotMatchingYourselfException();
        }
    }

    private void checkIfAlreadyRequested(Member loginMember, Member targetMember){
        if(matchingRequestRepository.existsByMembers(loginMember, targetMember)){
            throw new MatchingRequestAlreadyExitsException();
        }
    }

    public void deleteMatchingRequest(PrincipalDetails principalDetails, Long memberId){
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);
        MatchingRequest matchingRequest = getMatchingRequestByMembers(loginMember, targetMember);
        matchingRequestRepository.delete(matchingRequest);
    }

    public MatchingRequest getMatchingRequestByMembers(Member loginMember, Member targetMember){
        return matchingRequestRepository.findByMembers(loginMember, targetMember)
                .orElseThrow(MatchingRequestNotExistException::new);
    }
}
