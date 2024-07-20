package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.repository.preferenceorder.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceOrderService {

    private final PreferenceOrderRepository preferenceOrderRepository;
    private final MemberRepository memberRepository;

    public void setPreferenceOrder(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        ifAlreadySetPreferenceOrderDeleteBy(loginMember);

        savePreference(loginMember, requestDto.firstPreferenceType(), requestDto.firstPreference(), 1);
        savePreference(loginMember, requestDto.secondPreferenceType(), requestDto.secondPreference(), 2);
        savePreference(loginMember, requestDto.thirdPreferenceType(), requestDto.thirdPreference(), 3);
        savePreference(loginMember, requestDto.fourthPreferenceType(), requestDto.fourthPreference(), 4);
    }

    private void savePreference(Member member, String preferenceTypeStr, String preferenceStr, Integer order) {
        LifestyleType preferenceType = LifestyleType.fromType(preferenceTypeStr);
        Enum<?> preferenceDetail = preferenceType.getLifestyleValue(preferenceStr);
        preferenceOrderRepository.save(new PreferenceOrder(member, preferenceType, preferenceDetail, order));
    }

    private void ifAlreadySetPreferenceOrderDeleteBy(Member loginMember) {
        // 이미 저장한 상태라면 삭제
        if (preferenceOrderRepository.existsByMemberId(loginMember.getId())) {
            preferenceOrderRepository.deleteByMember(loginMember);
        }
    }

    public PreferenceOrderResponseDto findPreferenceOrder(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        List<PreferenceOrder> preferenceOrders =
                preferenceOrderRepository.findAllByMember(member);

        return PreferenceOrderResponseDto.fromEntity(preferenceOrders);
    }
}
