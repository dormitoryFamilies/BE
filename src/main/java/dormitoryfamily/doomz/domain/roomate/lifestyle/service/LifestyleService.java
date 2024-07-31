package dormitoryfamily.doomz.domain.roomate.lifestyle.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.lifestyle.exception.AlreadyRegisterMyLifestyleException;
import dormitoryfamily.doomz.domain.roomate.lifestyle.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roomate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class LifestyleService {

    private final LifestyleRepository lifestyleRepository;
    private final MemberRepository memberRepository;

    public void saveMyLifestyle(CreateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails){
        Member loginMember = principalDetails.getMember();
        checkAlreadySetLifestyle(loginMember);
        Lifestyle lifeStyle = CreateMyLifestyleRequestDto.toEntity(loginMember, requestDto);

        lifestyleRepository.save(lifeStyle);
    }

    private void checkAlreadySetLifestyle(Member loginMember) {
        if (lifestyleRepository.existsByMemberId(loginMember.getId())) {
            throw new AlreadyRegisterMyLifestyleException();
        }
    }

    public void updateMyLifestyle(UpdateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Lifestyle lifestyle = getLifestyleByMember(loginMember);

        lifestyle.updateMyLifestyle(requestDto);
    }

    private Lifestyle getLifestyleByMember(Member loginMember) {
        return lifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(LifestyleNotExistsException::new);
    }

    @Transactional(readOnly = true)
    public LifestyleResponseDto findLifestyle(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
        Lifestyle lifestyle = getLifestyleByMember(member);

        return LifestyleResponseDto.fromEntity(lifestyle);
    }

    /**
     * 개발용 API
     * 삭제 예정
     */
    @Transactional
    public void deleteMyLifestyle(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        lifestyleRepository.deleteByMember(loginMember);
    }
}
