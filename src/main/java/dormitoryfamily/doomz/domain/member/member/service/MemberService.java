package dormitoryfamily.doomz.domain.member.member.service;

import dormitoryfamily.doomz.domain.member.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.*;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.entity.type.RoleType;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.exception.NicknameDuplicatedException;
import dormitoryfamily.doomz.domain.member.member.exception.NotRoleMemberException;
import dormitoryfamily.doomz.domain.member.member.exception.NotVisitorOrRejectedMemberRoleException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingResultRepository;
import dormitoryfamily.doomz.domain.roommate.wish.repository.RoommateWishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final RoommateWishRepository roommateWishRepository;
    private final MatchingResultRepository matchingResultRepository;

    public NicknameCheckResponseDto checkNickname(String nickname) {
        boolean isDuplicated = memberRepository.existsByNickname(nickname);
        if (isDuplicated) {
            throw new NicknameDuplicatedException();
        }

        return new NicknameCheckResponseDto(isDuplicated);
    }

    public void setUpProfile(MemberSetUpProfileRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = getLoginMember(principalDetails);
        RoleType authority = loginMember.getAuthority();

        if (authority != RoleType.ROLE_VISITOR && authority != RoleType.ROLE_REJECTED_MEMBER) {
            throw new NotVisitorOrRejectedMemberRoleException();
        }
        loginMember.setUpProfile(requestDto);
    }

    private Member getLoginMember(PrincipalDetails principalDetails) {
        return memberRepository.findById(principalDetails.getMember().getId())
                .orElseThrow(MemberNotExistsException::new);
    }

    public MemberProfilePagingListResponseDto findAllMembers(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Page<Member> membersPage = memberRepository.findAllVerifiedMembers(pageable);
        List<AllMembersResponseDto> memberDtos = convertToDtoList(membersPage.getContent(), loginMember);

        return MemberProfilePagingListResponseDto.from(membersPage, memberDtos);
    }

    public MemberProfilePagingListResponseDto searchMembers(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Page<Member> members = memberRepository.findVerifiedMembersByKeyword(requestDto.q(), pageable);
        List<AllMembersResponseDto> memberDtos = convertToDtoList(members.getContent(), loginMember);

        return MemberProfilePagingListResponseDto.from(members, memberDtos);
    }

    private List<AllMembersResponseDto> convertToDtoList(List<Member> members, Member loginMember) {
        return members.stream()
                .map(member -> {
                    boolean isFollowing = followRepository.existsByFollowerAndFollowing(loginMember, member);
                    boolean isRoommateWished = roommateWishRepository.existsByWisherAndWished(loginMember, member);
                    return AllMembersResponseDto.fromEntity(member, isFollowing, isRoommateWished);
                })
                .collect(Collectors.toList());
    }

    public MemberDetailsResponseDto getMemberProfile(Long memberId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Member targetMember = getMemberById(memberId);

        boolean isFollowing = followRepository.existsByFollowerAndFollowing(loginMember, targetMember);

        return MemberDetailsResponseDto.fromEntity(targetMember, isFollowing);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    public MyProfileResponseDto getMyProfile(PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        return MyProfileResponseDto.fromEntity(member);
    }

    public void modifyMyProfile(MyProfileModifyRequestDto requestDto, PrincipalDetails principalDetails) {
        Member member = getMemberById(principalDetails.getMember().getId());
        member.updateProfile(requestDto);
    }

    public RoommateMatchingMemberProfileResponseDto findRoommateProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
        return RoommateMatchingMemberProfileResponseDto.fromEntity(member);
    }

    public NonVerifiedStudentCardsResponseDto getNonVerifiedStudentCards(Pageable pageable) {
        Page<Member> nonVerifiedMembersPage = memberRepository.findNonVerifiedMember(pageable);
        return NonVerifiedStudentCardsResponseDto.fromResponseDto(nonVerifiedMembersPage);
    }

    public void approveStudentCard(Long memberId) {
        Member member = getMemberById(memberId);
        validateIsRoleMember(member);
        member.authenticateStudentCard();
    }


    public void rejectStudentCard(Long memberId) {
        Member member = getMemberById(memberId);
        validateIsRoleMember(member);
        member.rejectStudentCard();
    }

    private void validateIsRoleMember(Member member) {
        if (member.getAuthority() != RoleType.ROLE_MEMBER) {
            throw new NotRoleMemberException();
        }
    }

    public MatchingStatusResponseDto getMyMatchedId(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Optional<MatchingResult> matchingResult = matchingResultRepository.findBySenderOrReceiver(loginMember);

        Long matchedId = matchingResult.map(result -> {
            if (result.getSender().getId().equals(loginMember.getId())) {
                return result.getReceiver().getId();
            } else {
                return result.getSender().getId();
            }
        }).orElse(0L);

        return MatchingStatusResponseDto.from(matchedId);
    }

    public MemberIdResponseDto findMyMemberId(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        return MemberIdResponseDto.from(loginMember);
    }

    /**
     * 개발용 임시 메소드
     * 삭제 예정
     */
    public void changeMyAuthority(PrincipalDetails principalDetails, String newAuthority) {
        Member loginMember = principalDetails.getMember();

        RoleType roleType = switch (newAuthority) {
            case "ROLE_VISITOR" -> RoleType.ROLE_VISITOR;
            case "ROLE_MEMBER" -> RoleType.ROLE_MEMBER;
            case "ROLE_REJECTED_MEMBER" -> RoleType.ROLE_REJECTED_MEMBER;
            case "ROLE_VERIFIED_STUDENT" -> RoleType.ROLE_VERIFIED_STUDENT;
            case "ROLE_ADMIN" -> RoleType.ROLE_ADMIN;
            default -> throw new IllegalArgumentException("잘못된 권한 값입니다: " + newAuthority);
        };

        memberRepository.changeMyAuthority(roleType, loginMember.getId());
    }
}
