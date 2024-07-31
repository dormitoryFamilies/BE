package dormitoryfamily.doomz.domain.roommate.matching.service;

import dormitoryfamily.doomz.domain.roommate.matching.dto.response.MatchingRequestCountResponseDto;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.roommate.matching.exception.*;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingRequestRepository;
import dormitoryfamily.doomz.domain.roommate.matching.util.StatusType;
import dormitoryfamily.doomz.domain.member.member.dto.response.MatchingRequestMemberResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        checkMatchingRequestAlreadyExits(loginMember, targetMember);
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

    private void checkMatchingRequestAlreadyExits(Member loginMember, Member targetMember) {
        if (matchingRequestRepository.findByMembers(loginMember, targetMember).isPresent()) {
            throw new MatchingRequestAlreadyExitsException();
        }
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

    public MemberProfilePagingListResponseDto findMyMatchingRequest(PrincipalDetails principalDetails, String status, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        StatusType statusType = StatusType.fromString(status);

        Page<MatchingRequest> matchingRequests = getMatchingRequests(loginMember, statusType, pageable);
        List<MatchingRequestMemberResponseDto> matchingRequestMemberResponseDtos = convertToDtoList(matchingRequests.getContent(), loginMember, statusType);

        return MemberProfilePagingListResponseDto.from(matchingRequests, matchingRequestMemberResponseDtos);
    }

    private Page<MatchingRequest> getMatchingRequests(Member loginMember, StatusType statusType, Pageable pageable) {
        return statusType == StatusType.SENT
                ? matchingRequestRepository.findBySenderOrderByCreatedAtDesc(loginMember, pageable)
                : matchingRequestRepository.findByReceiverOrderByCreatedAtDesc(loginMember, pageable);
    }

    private List<MatchingRequestMemberResponseDto> convertToDtoList(List<MatchingRequest> matchingRequests, Member loginMember, StatusType statusType) {
        return matchingRequests.stream()
                .map(matchingRequest -> createResponseDto(matchingRequest, loginMember, statusType))
                .collect(Collectors.toList());
    }

    private MatchingRequestMemberResponseDto createResponseDto(MatchingRequest matchingRequest, Member loginMember, StatusType statusType) {
        Member targetMember = statusType == StatusType.SENT ? matchingRequest.getReceiver() : matchingRequest.getSender();
        boolean isMatchable = isRoommateMatchAvailable(loginMember, targetMember);
        return MatchingRequestMemberResponseDto.fromEntity(targetMember, isMatchable);
    }

    private boolean isRoommateMatchAvailable(Member loginMember, Member member) {
        return !member.isRoommateMatched() && Objects.equals(loginMember.getDormitoryType(), member.getDormitoryType());
    }

    public MatchingRequestCountResponseDto countMyReceivedRequest(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        long count = matchingRequestRepository.countMatchingRequestsByReceiver(loginMember);
        return MatchingRequestCountResponseDto.from(loginMember, count);
    }
}

