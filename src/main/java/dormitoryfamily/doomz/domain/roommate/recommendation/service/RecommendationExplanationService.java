package dormitoryfamily.doomz.domain.roommate.recommendation.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.dto.CandidateWithExplanationDto;
import dormitoryfamily.doomz.domain.roommate.recommendation.util.LifestyleComparisonBuilder;
import dormitoryfamily.doomz.global.openai.OpenAIClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationExplanationService {

    private static final String SYSTEM_PROMPT = """
            당신은 대학교 기숙사 룸메이트 매칭 서비스의 AI 어시스턴트입니다.
            두 학생의 라이프스타일을 비교하여 왜 좋은 룸메이트가 될 수 있는지
            한국어로 친근하게 설명해주세요.

            규칙:
            1. 2-3문장으로 간결하게 작성
            2. 긍정적인 톤 유지
            3. 사용자의 우선순위를 고려하여 중요한 항목 강조
            4. 반말이 아닌 존댓말 사용
            5. 구체적인 공통점이나 호환성을 언급
            """;

    private final OpenAIClient openAIClient;
    private final LifestyleRepository lifestyleRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public List<CandidateWithExplanationDto> getExplanationsForCandidates(Member user, List<Long> candidateIds) {
        List<CandidateWithExplanationDto> results = new ArrayList<>();

        Optional<Lifestyle> userLifestyleOpt = lifestyleRepository.findByMemberId(user.getId());
        Optional<PreferenceOrder> userPreferenceOpt = preferenceOrderRepository.findByMember(user);

        if (userLifestyleOpt.isEmpty() || userPreferenceOpt.isEmpty()) {
            log.warn("사용자의 라이프스타일 또는 선호도 정보가 없습니다: memberId={}", user.getId());
            for (Long candidateId : candidateIds) {
                results.add(CandidateWithExplanationDto.of(candidateId, getDefaultExplanation()));
            }
            return results;
        }

        Lifestyle userLifestyle = userLifestyleOpt.get();
        PreferenceOrder userPreference = userPreferenceOpt.get();

        for (Long candidateId : candidateIds) {
            String explanation = getOrGenerateExplanation(user.getId(), candidateId, userLifestyle, userPreference);
            results.add(CandidateWithExplanationDto.of(candidateId, explanation));
        }

        return results;
    }

    private String getOrGenerateExplanation(
            Long userId,
            Long candidateId,
            Lifestyle userLifestyle,
            PreferenceOrder userPreference
    ) {
        String cacheKey = REDIS_EXPLANATION_KEY_PREFIX + userId + ":" + candidateId;

        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached.toString();
        }

        Optional<Lifestyle> candidateLifestyleOpt = lifestyleRepository.findByMemberId(candidateId);
        if (candidateLifestyleOpt.isEmpty()) {
            return getDefaultExplanation();
        }

        Lifestyle candidateLifestyle = candidateLifestyleOpt.get();
        String explanation = generateExplanation(userLifestyle, candidateLifestyle, userPreference);

        redisTemplate.opsForValue().set(cacheKey, explanation, EXPLANATION_CACHE_DURATION);

        return explanation;
    }

    private String generateExplanation(
            Lifestyle userLifestyle,
            Lifestyle candidateLifestyle,
            PreferenceOrder userPreference
    ) {
        String comparisonText = LifestyleComparisonBuilder.buildComparisonText(
                userLifestyle, candidateLifestyle, userPreference
        );

        return openAIClient.chatCompletion(SYSTEM_PROMPT, comparisonText)
                .map(String::trim)
                .orElseGet(() -> LifestyleComparisonBuilder.buildFallbackExplanation(userLifestyle, candidateLifestyle));
    }

    private String getDefaultExplanation() {
        return "라이프스타일을 종합적으로 고려했을 때 좋은 룸메이트가 될 수 있어요!";
    }
}
