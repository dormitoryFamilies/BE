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
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

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

    public MemberProfileListResponseDto findAllMembers(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();

        List<Member> members = memberRepository.findAllMembersExcludingFollowed(loginMember.getId());

        List<MemberInfoResponseDto> memberInfoDtos = members.stream()
                .map(MemberInfoResponseDto::fromEntity)
                .toList();
        return MemberProfileListResponseDto.from(memberInfoDtos);
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

    public MemberProfileListResponseDto searchMembers(PrincipalDetails principalDetails, SearchRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();

        List<Member> members = memberRepository.findMembersExcludingFollowed(loginMember.getId(), requestDto.q());

        List<MemberInfoResponseDto> memberInfoDtos = members.stream()
                .map(MemberInfoResponseDto::fromEntity)
                .toList();

        return MemberProfileListResponseDto.from(memberInfoDtos);
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
}
