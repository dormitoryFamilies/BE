package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request.PreferenceLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceLifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.repository.preferencelifestyle.PreferenceRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceLifestyleService {

    private final MemberRepository memberRepository;
    private final PreferenceRepository preferenceRepository;

    public void savePreferenceLifestyle(PreferenceLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member member = memberRepository.findById(principalDetails.getMember().getId()).get();

        savePreference(member, requestDto.firstPreferenceType(), requestDto.firstPreference(), 1);
        savePreference(member, requestDto.secondPreferenceType(), requestDto.secondPreference(), 2);
        savePreference(member, requestDto.thirdPreferenceType(), requestDto.thirdPreference(), 3);
        savePreference(member, requestDto.fourthPreferenceType(), requestDto.fourthPreference(), 4);
    }

    private void savePreference(Member member, String preferenceTypeStr, String preferenceStr, int order) {
        LifestyleType preferenceType = LifestyleType.fromString(preferenceTypeStr);
        Enum<?> preferenceDetail = preferenceType.getEnumValue(preferenceStr);
        preferenceRepository.save(new PreferenceLifestyle(member, preferenceType, preferenceDetail, order));
    }
}
