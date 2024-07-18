package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.response.MyLifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.request.MyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
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

    public MyLifestyleResponseDto saveMyLifestyle(MyLifestyleRequestDto requestDto, PrincipalDetails principalDetails){
        Member loginMember = principalDetails.getMember();
        MyLifestyle myLifeStyle = MyLifestyleRequestDto.toEntity(loginMember, requestDto);
        myLifestyleRepository.save(myLifeStyle);
        return MyLifestyleResponseDto.fromEntity(myLifeStyle);
    }
}
