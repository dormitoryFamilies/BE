package dormitoryfamily.doomz.domain.member.service;

import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MyProfileResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberProfileResponseDto getMemberProfile(Long memberId){
        Member member = getMemberById(memberId);
        return  MemberProfileResponseDto.fromEntity(member);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    public MyProfileResponseDto getMyProfile(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        return MyProfileResponseDto.fromEntity(loginMember);
    }
}
