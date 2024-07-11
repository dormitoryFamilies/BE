package dormitoryfamily.doomz.domain.member.service;

import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.*;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.exception.NicknameDuplicatedException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void setUpProfile(
            MemberSetUpProfileRequestDto requestDto,
            PrincipalDetails principalDetails) {
        Member loginMember = getMember(principalDetails);
        loginMember.setUpProfile(requestDto);
    }

    private Member getMember(PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return null;
        }
        return memberRepository.findById(principalDetails.getMember().getId())
                .orElseThrow(MemberNotFoundException::new);
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

    public MemberInfoListResponseDto searchMembers(PrincipalDetails principalDetails, SearchRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        List<Member> members = memberRepository.findMembersExcludingFollowed(loginMember.getId(), requestDto.q());
        List<MemberInfoResponseDto> memberDtos = members.stream().map(MemberInfoResponseDto::fromEntity).collect(Collectors.toList());
        return MemberInfoListResponseDto.toDto(memberDtos);
    }
}
