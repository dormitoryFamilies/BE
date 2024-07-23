package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotFoundException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.exception.AlreadyRegisterPreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.exception.DuplicatePreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roomate.repository.preferenceorder.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

    public void setPreferenceOrders(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySavedPreferenceOrder(loginMember);
        checkForDuplicatePreferenceOrder(requestDto);

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

    private void checkAlreadySavedPreferenceOrder(Member loginMember) {
        if (isExistPreferenceOrder(loginMember)) {
            throw new AlreadyRegisterPreferenceOrderException();
        }
    }

    private boolean isExistPreferenceOrder(Member loginMember) {
        return preferenceOrderRepository.existsByMemberId(loginMember.getId());
    }

    @Transactional(readOnly = true)
    public PreferenceOrderResponseDto findPreferenceOrder(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        List<PreferenceOrder> preferenceOrders =
                preferenceOrderRepository.findAllByMember(member);

        return PreferenceOrderResponseDto.fromEntity(preferenceOrders);
    }

    public void updatePreferenceOrders(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySetPreferenceOrder(loginMember);
        checkForDuplicatePreferenceOrder(requestDto);

        updatePreferenceOrder(loginMember, requestDto.firstPreferenceType(), requestDto.firstPreference(), PRIORITY_LEVEL_1);
        updatePreferenceOrder(loginMember, requestDto.secondPreferenceType(), requestDto.secondPreference(), PRIORITY_LEVEL_2);
        updatePreferenceOrder(loginMember, requestDto.thirdPreferenceType(), requestDto.thirdPreference(), PRIORITY_LEVEL_3);
        updatePreferenceOrder(loginMember, requestDto.fourthPreferenceType(), requestDto.fourthPreference(), PRIORITY_LEVEL_4);
    }

    private void checkAlreadySetPreferenceOrder(Member loginMember) {
        if (!isExistPreferenceOrder(loginMember)) {
            throw new PreferenceOrderNotExistsException();
        }
    }

    private void updatePreferenceOrder(Member loginMember, String preferenceTypeStr, String preferenceStr, Integer order) {
        PreferenceOrder foundOrder = preferenceOrderRepository.findByMemberAndPreferenceOrder(loginMember, order);
        LifestyleType firstLifestyleType = fromType(preferenceTypeStr);
        foundOrder.updateOrder(firstLifestyleType, firstLifestyleType.getLifestyleValue(preferenceStr));
    }

    /**
     * DTO 에 중복 타입이 포함되어 있는지 여부 확인
     */
    private void checkForDuplicatePreferenceOrder(PreferenceOrderRequestDto requestDto) {
        HashSet<String> preferences = new HashSet<>();

        preferences.add(requestDto.firstPreferenceType());
        if (!preferences.add(requestDto.secondPreferenceType())) {
            throw new DuplicatePreferenceOrderException(requestDto.secondPreferenceType());
        }
        if (!preferences.add(requestDto.thirdPreferenceType())) {
            throw new DuplicatePreferenceOrderException(requestDto.thirdPreferenceType());
        }
        if (!preferences.add(requestDto.fourthPreferenceType())) {
            throw new DuplicatePreferenceOrderException(requestDto.fourthPreferenceType());
        }
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
