package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request.CreatePreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.exception.AlreadyRegisterPreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.exception.MissingPreferenceDetailParameterException;
import dormitoryfamily.doomz.domain.roomate.exception.MissingPreferenceTypeParameterException;
import dormitoryfamily.doomz.domain.roomate.repository.preferenceorder.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType.fromType;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceOrderService {

    private final PreferenceOrderRepository preferenceOrderRepository;
    private final MemberRepository memberRepository;

    private static final Integer PRIORITY_LEVEL_1 = 1;
    private static final Integer PRIORITY_LEVEL_2 = 2;
    private static final Integer PRIORITY_LEVEL_3 = 3;
    private static final Integer PRIORITY_LEVEL_4 = 4;

    public void setPreferenceOrder(CreatePreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        ifAlreadySavePreferenceOrderBy(loginMember);

        savePreference(loginMember, requestDto.firstPreferenceType(), requestDto.firstPreference(), PRIORITY_LEVEL_1);
        savePreference(loginMember, requestDto.secondPreferenceType(), requestDto.secondPreference(), PRIORITY_LEVEL_2);
        savePreference(loginMember, requestDto.thirdPreferenceType(), requestDto.thirdPreference(), PRIORITY_LEVEL_3);
        savePreference(loginMember, requestDto.fourthPreferenceType(), requestDto.fourthPreference(), PRIORITY_LEVEL_4);
    }

    private void savePreference(Member member, String preferenceTypeStr, String preferenceStr, Integer order) {
        LifestyleType preferenceType = fromType(preferenceTypeStr);
        Enum<?> preferenceDetail = preferenceType.getLifestyleValue(preferenceStr);
        preferenceOrderRepository.save(new PreferenceOrder(member, preferenceType, preferenceDetail, order));
    }

    private void ifAlreadySavePreferenceOrderBy(Member loginMember) {
        if (preferenceOrderRepository.existsByMemberId(loginMember.getId())) {
            throw new AlreadyRegisterPreferenceOrderException();
        }
    }

    public PreferenceOrderResponseDto findPreferenceOrder(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        List<PreferenceOrder> preferenceOrders =
                preferenceOrderRepository.findAllByMember(member);

        return PreferenceOrderResponseDto.fromEntity(preferenceOrders);
    }

    public void updatePreferenceOrder(CreatePreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();

        // 1순위 값이 있다면 변경
        if (isPreferencePairValid(requestDto.firstPreferenceType(), requestDto.firstPreference())) {
            LifestyleType firstLifestyleType = fromType(requestDto.firstPreferenceType());

            PreferenceOrder foundOrder = preferenceOrderRepository.findByMemberAndPreferenceOrder(loginMember, PRIORITY_LEVEL_1);
            foundOrder.updateOrder(firstLifestyleType, firstLifestyleType.getLifestyleValue(requestDto.firstPreference())
            );
        }

        // 2순위 값이 있다면 변경
        if (isPreferencePairValid(requestDto.secondPreferenceType(), requestDto.secondPreference())) {
            LifestyleType secondLifestyleType = fromType(requestDto.secondPreferenceType());

            PreferenceOrder foundOrder = preferenceOrderRepository.findByMemberAndPreferenceOrder(loginMember, PRIORITY_LEVEL_2);
            foundOrder.updateOrder(secondLifestyleType, secondLifestyleType.getLifestyleValue(requestDto.secondPreference())
            );
        }

        // 3순위 값이 있다면 변경
        if (isPreferencePairValid(requestDto.thirdPreferenceType(), requestDto.thirdPreference())) {
            LifestyleType thirdPreferenceType = fromType(requestDto.thirdPreferenceType());

            PreferenceOrder foundOrder = preferenceOrderRepository.findByMemberAndPreferenceOrder(loginMember, PRIORITY_LEVEL_3);
            foundOrder.updateOrder(thirdPreferenceType, thirdPreferenceType.getLifestyleValue(requestDto.thirdPreference())
            );
        }

        // 4순위 값이 있다면 변경
        if (isPreferencePairValid(requestDto.fourthPreferenceType(), requestDto.fourthPreference())) {
            LifestyleType fourthPreferenceType = fromType(requestDto.fourthPreferenceType());

            PreferenceOrder foundOrder = preferenceOrderRepository.findByMemberAndPreferenceOrder(loginMember, PRIORITY_LEVEL_4);
            foundOrder.updateOrder(fourthPreferenceType, fourthPreferenceType.getLifestyleValue(requestDto.fourthPreference())
            );
        }
    }

    private boolean isPreferencePairValid(String type, String detail) {
        if (type == null && detail == null) {
            return false;
        } else if (type != null && detail == null) {
            throw new MissingPreferenceDetailParameterException(type);
        } else if (type == null && detail != null) {
            throw new MissingPreferenceTypeParameterException(detail);
        }
        return true;
    }

    /**
     * 개발용 API
     * 삭제 예정
     */
    public void deleteMyLifestyle(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        preferenceOrderRepository.deleteByMember(loginMember);
    }
}
