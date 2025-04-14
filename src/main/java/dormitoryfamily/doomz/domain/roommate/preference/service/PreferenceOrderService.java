package dormitoryfamily.doomz.domain.roommate.preference.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.preference.dto.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roommate.preference.dto.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleAttribute;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roommate.preference.exception.AlreadyRegisterPreferenceOrderException;
import dormitoryfamily.doomz.domain.roommate.preference.exception.DuplicatePreferenceOrderException;
import dormitoryfamily.doomz.domain.roommate.preference.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleType.fromType;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceOrderService {

    private final PreferenceOrderRepository preferenceOrderRepository;
    private final MemberRepository memberRepository;
    private final ElasticsearchClient elasticsearchClient;

    private static final String PREFERENCE_INDEX = "preference_vectors";

    public void setPreferenceOrders(PreferenceOrderRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySavedPreferenceOrder(loginMember);
        checkForDuplicatePreferenceOrder(requestDto);

        PreferenceOrder order = PreferenceOrder.builder()
                .member(loginMember)
                .firstPreferenceOrder(getPreference(requestDto.firstPreference()))
                .secondPreferenceOrder(getPreference(requestDto.secondPreference()))
                .thirdPreferenceOrder(getPreference(requestDto.thirdPreference()))
                .fourthPreferenceOrder(getPreference(requestDto.fourthPreference()))
                .build();

        preferenceOrderRepository.save(order);
        indexPreferenceVector(loginMember.getId(), order, loginMember);
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

        indexPreferenceVector(loginMember.getId(), preferenceOrder, loginMember);
    }

    private void indexPreferenceVector(Long memberId, PreferenceOrder order, Member member) {
        try {
            float[] weightVector = new float[11];
            float[] preferredValues = new float[11];
            for (int i = 0; i < 11; i++) {
                weightVector[i] = 0.1f;
                preferredValues[i] = 0.0f;
            }

            setWeightAndValue(weightVector, preferredValues, order.getFirstPreferenceOrder(), 1.0f);
            setWeightAndValue(weightVector, preferredValues, order.getSecondPreferenceOrder(), 0.7f);
            setWeightAndValue(weightVector, preferredValues, order.getThirdPreferenceOrder(), 0.5f);
            setWeightAndValue(weightVector, preferredValues, order.getFourthPreferenceOrder(), 0.2f);

            Map<String, Object> document = new HashMap<>();
            document.put("member_id", memberId);
            document.put("preference_weight", weightVector);
            document.put("preferred_values", preferredValues);
            document.put("dormitory", member.getDormitoryType().name());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(PREFERENCE_INDEX)
                    .id(memberId.toString())
                    .document(document));

            IndexResponse response = elasticsearchClient.index(request);
            if (response.result() != Result.Created && response.result() != Result.Updated) {
                System.err.println("⚠️ 저장 예외 상태: " + response.result());
            }
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch preference vector 저장 실패", e);
        }
    }

    private void setWeightAndValue(float[] vector, float[] values, Enum<?> preference, float weight) {
        LifestyleType type = LifestyleType.fromTypeName(preference.getClass().getSimpleName());
        int index = type.getVectorIndex();
        if (index >= 0 && index < vector.length) {
            vector[index] = weight;
            values[index] = ((LifestyleAttribute) preference).getIndex();
        }
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

    public void deleteMyLifestyle(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        preferenceOrderRepository.deleteByMember(loginMember);
    }
}
