package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
import dormitoryfamily.doomz.domain.roomate.exception.AlreadyRegisterMyLifestyleException;
import dormitoryfamily.doomz.domain.roomate.exception.MyLifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roomate.repository.mylifestyle.MyLifestyleRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MyLifestyleService {

    private final MyLifestyleRepository myLifestyleRepository;

    public void saveMyLifestyle(CreateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails){
        Member loginMember = principalDetails.getMember();
        checkAlreadySetLifestyle(loginMember);
        MyLifestyle myLifeStyle = CreateMyLifestyleRequestDto.toEntity(loginMember, requestDto);

        myLifestyleRepository.save(myLifeStyle);
    }

    private void checkAlreadySetLifestyle(Member loginMember) {
        if (myLifestyleRepository.existsByMemberId(loginMember.getId())) {
            throw new AlreadyRegisterMyLifestyleException();
        }
    }

    public void updateMyLifestyle(UpdateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        MyLifestyle myLifestyle = getLifestyleByMemberId(loginMember);

        myLifestyle.updateMyLifestyle(requestDto);
    }

    private MyLifestyle getLifestyleByMemberId(Member loginMember) {
        return myLifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(MyLifestyleNotExistsException::new);
    }

    public LifestyleResponseDto findMyLifestyle(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        MyLifestyle myLifestyle = getLifestyleByMemberId(loginMember);

        return LifestyleResponseDto.fromEntity(myLifestyle);
    }
}
