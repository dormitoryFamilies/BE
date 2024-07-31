package dormitoryfamily.doomz.domain.roomate.preference.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.preference.dto.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.preference.dto.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roomate.preference.exception.AlreadyRegisterPreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.preference.exception.DuplicatePreferenceOrderException;
import dormitoryfamily.doomz.domain.roomate.preference.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roomate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type.LifestyleType.fromType;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceOrderService {

    private final PreferenceOrderRepository preferenceOrderRepository;
    private final MemberRepository memberRepository;

    //todo. 애노테이션으로 필수값인지 확인하는 유효성 검사 추가하기
    public void setPreferenceOrders(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySavedPreferenceOrder(loginMember);
        checkForDuplicatePreferenceOrder(requestDto);

        preferenceOrderRepository.save(PreferenceOrder.builder()
                .member(loginMember)
                .firstPreferenceOrder(getPreference(requestDto.firstPreference()))
                .secondPreferenceOrder(getPreference(requestDto.secondPreference()))
                .thirdPreferenceOrder(getPreference(requestDto.thirdPreference()))
                .fourthPreferenceOrder(getPreference(requestDto.fourthPreference()))
                .build()
        );
    }

    private Enum<?> getPreference(String preferenceTypeInput) {
        String[] preferenceOrder = preferenceTypeInput.split(":");
        LifestyleType preferenceType = fromType(preferenceOrder[0]);
        return preferenceType.getLifestyleValueFrom(preferenceOrder[1]);
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
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);

        return PreferenceOrderResponseDto.fromEntity(getPreferenceOrder(member));
    }

    private PreferenceOrder getPreferenceOrder(Member member) {
        return preferenceOrderRepository.findByMember(member)
                .orElseThrow(PreferenceOrderNotExistsException::new);
    }

    public void updatePreferenceOrders(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkForDuplicatePreferenceOrder(requestDto);

        PreferenceOrder preferenceOrder = getPreferenceOrder(loginMember);

        preferenceOrder.updateOrder(
                getPreference(requestDto.firstPreference()),
                getPreference(requestDto.secondPreference()),
                getPreference(requestDto.thirdPreference()),
                getPreference(requestDto.fourthPreference())
        );
    }

    /**
     * DTO 에 중복 타입이 포함되어 있는지 여부 확인
     */
    private void checkForDuplicatePreferenceOrder(PreferenceOrderRequestDto requestDto) {
        HashSet<String> preferences = new HashSet<>();

        preferences.add(requestDto.firstPreference());
        if (!preferences.add(requestDto.secondPreference())) {
            throw new DuplicatePreferenceOrderException(requestDto.secondPreference());
        }
        if (!preferences.add(requestDto.thirdPreference())) {
            throw new DuplicatePreferenceOrderException(requestDto.thirdPreference());
        }
        if (!preferences.add(requestDto.fourthPreference())) {
            throw new DuplicatePreferenceOrderException(requestDto.fourthPreference());
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
