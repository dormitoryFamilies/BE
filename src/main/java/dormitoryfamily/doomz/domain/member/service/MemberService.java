package dormitoryfamily.doomz.domain.member.service;

import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.NicknameCheckResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.NicknameDuplicatedException;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public NicknameCheckResponseDto checkNickname(String nickname) {
        boolean isDuplicated = memberRepository.existsByNickname(nickname);
        if (isDuplicated) {
            throw new NicknameDuplicatedException();
        }

        return new NicknameCheckResponseDto(isDuplicated);
    }

    @Transactional
    public void setUpProfile(
            MemberSetUpProfileRequestDto requestDto,
            PrincipalDetails principalDetails)
    {
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
