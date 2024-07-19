package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request.PreferenceLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceLifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.exception.AlreadyRegisterPreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.repository.preferencelifestyle.PreferenceRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceLifestyleService {

    private final PreferenceRepository preferenceRepository;

    public void savePreferenceLifestyle(PreferenceLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySetPreferenceOrder(loginMember);

        savePreference(loginMember, requestDto.firstPreferenceType(), requestDto.firstPreference(), 1);
        savePreference(loginMember, requestDto.secondPreferenceType(), requestDto.secondPreference(), 2);
        savePreference(loginMember, requestDto.thirdPreferenceType(), requestDto.thirdPreference(), 3);
        savePreference(loginMember, requestDto.fourthPreferenceType(), requestDto.fourthPreference(), 4);
    }

    private void savePreference(Member member, String preferenceTypeStr, String preferenceStr, Integer order) {
        LifestyleType preferenceType = LifestyleType.fromType(preferenceTypeStr);
        Enum<?> preferenceDetail = preferenceType.getLifestyleValue(preferenceStr);
        preferenceRepository.save(new PreferenceLifestyle(member, preferenceType, preferenceDetail, order));
    }

    private void checkAlreadySetPreferenceOrder(Member loginMember) {
        if (preferenceRepository.existsByMemberId(loginMember.getId())) {
            throw new AlreadyRegisterPreferenceOrderException();
        }
    }
}
