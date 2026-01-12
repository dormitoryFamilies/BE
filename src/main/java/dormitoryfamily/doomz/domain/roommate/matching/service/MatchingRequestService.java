package dormitoryfamily.doomz.domain.roommate.matching.service;

import dormitoryfamily.doomz.domain.member.member.dto.response.MatchingRequestMemberResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.roommate.matching.dto.response.MatchingRequestCountResponseDto;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.roommate.matching.entity.RequestStatus;
import dormitoryfamily.doomz.domain.roommate.matching.event.request.MatchingRequestEvent;
import dormitoryfamily.doomz.domain.roommate.matching.exception.*;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingRequestRepository;
import dormitoryfamily.doomz.domain.roommate.matching.util.StatusType;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.*;
import static dormitoryfamily.doomz.domain.roommate.matching.entity.RequestStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingRequestService {

    private final MatchingRequestRepository matchingRequestRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void saveMatchingRequest(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        validateMatchingRequestCapability(loginMember, targetMember);

        MatchingRequest matchingRequest = MatchingRequest.createMatchingRequest(loginMember, targetMember);
        matchingRequestRepository.save(matchingRequest);

        notifyMatchingRequestInfo(matchingRequest, MATCHING_REQUEST);
    }

    private Member getMemberById (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void notifyMatchingRequestInfo(MatchingRequest matchingRequest, NotificationType notificationType) {
        eventPublisher.publishEvent(new MatchingRequestEvent(matchingRequest, notificationType));
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

            // PENDING 상태인 요청만 삭제
            int deleted = matchingRequestRepository.deleteByMembersAndStatus(loginMember, targetMember, PENDING);

            if (deleted == 0) {
                throw new MatchingRequestNotExistException();
            }

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

    public void acceptMatchingRequest(PrincipalDetails principalDetails, Long memberId) {
        Long loginMemberId = principalDetails.getMember().getId();

        Member loginMember = getMemberById(loginMemberId);
        Member targetMember = getMemberById(memberId);

        MatchingRequest matchingRequest = matchingRequestRepository
                .findBySenderAndReceiverAndStatus(targetMember, loginMember, PENDING)
                .orElseThrow(MatchingRequestNotExistException::new);

        // Member 매칭 상태 업데이트
        updateMemberMatchingStatus(loginMember, targetMember);

        // 매칭 요청 상태 업데이트 (PENDING -> ACCEPTED)
        int updated = matchingRequestRepository.updateStatus(
                matchingRequest.getId(),
                PENDING,
                ACCEPTED
        );

        if (updated == 0) {
            throw new MatchingConflictException();
        }

        notifyMatchingAcceptInfo(matchingRequest);
    }

    // 매칭 취소 (ACCEPTED -> 삭제)
    public void cancelMatchingRequest(PrincipalDetails principalDetails, Long memberId) {
        Long loginMemberId = principalDetails.getMember().getId();

        Member loginMember = getMemberById(loginMemberId);
        Member targetMember = getMemberById(memberId);

        // ACCEPTED 상태인 요청만 삭제 (CAS)
        int deleted = matchingRequestRepository.deleteByMembersAndStatus(loginMember, targetMember, ACCEPTED);

        if (deleted == 0) {
            throw new MatchingRequestNotExistException();
        }

        // Member 매칭 상태 해제
        memberRepository.markUnmatched(loginMember.getId());
        memberRepository.markUnmatched(targetMember.getId());
    }

    private void updateMemberMatchingStatus(Member loginMember, Member targetMember) {
        int updatedLogin = memberRepository.markMatched(loginMember.getId(), loginMember.getDormitoryType());
        int updatedTarget = memberRepository.markMatched(targetMember.getId(), loginMember.getDormitoryType());

        if (updatedLogin == 0 || updatedTarget == 0) {
            // 둘 중 하나라도 이미 매칭된 상태 (CAS 실패)
            throw new AlreadyMatchedMemberException();
        }
    }

    private void notifyMatchingAcceptInfo(MatchingRequest matchingRequest) {
        eventPublisher.publishEvent(new MatchingRequestEvent(matchingRequest, MATCHING_ACCEPT));
    }
}
