package dormitoryfamily.doomz.domain.member.service;

import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MyProfileResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.NicknameCheckResponseDto;
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

    public NicknameCheckResponseDto checkNickname(String nickname) {
        boolean isDuplicated = memberRepository.existsByNickname(nickname);
        if (isDuplicated) {
            throw new NicknameDuplicatedException();
        }

        return new NicknameCheckResponseDto(isDuplicated);
    }

    public MemberProfileResponseDto getMemberProfile(Long memberId) {
        Member member = getMemberById(memberId);
        return MemberProfileResponseDto.fromEntity(member);
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
        List<MemberProfileResponseDto> memberDtos = members.stream().map(MemberProfileResponseDto::fromEntity).collect(Collectors.toList());
        return MemberProfileListResponseDto.toDto(memberDtos);
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
}
