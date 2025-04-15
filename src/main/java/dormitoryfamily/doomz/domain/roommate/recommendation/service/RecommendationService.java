package dormitoryfamily.doomz.domain.roommate.recommendation.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.matching.exception.AlreadyMatchedMemberException;
import dormitoryfamily.doomz.domain.roommate.matching.service.MatchingRequestService;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.dto.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roommate.util.ScoreCalculator;
import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final MemberRepository memberRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final LifestyleRepository lifestyleRepository;
    private final MatchingRequestService matchingRequestService;
    private final ElasticScriptQueryExecutor elasticScriptQueryExecutor;
    private final RedisTemplate<String, Object> redisTemplate;

    public RecommendationResponseDto findTopCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadyMatched(loginMember);

        Long memberId = loginMember.getId();
        
        // 엘라스틱서치 벡터 쿼리 기반으로 추천 점수 계산
        List<Entry<Long, Double>> scores = findTopMatchingCandidatesWithVectorQuery(loginMember);

        // 결과가 없거나 오류가 발생한 경우 기존 방식으로 계산
        if (scores.isEmpty()) {
            log.warn("엘라스틱서치 벡터 쿼리 추천 결과가 없거나 처리 중 오류 발생, 데이터베이스 조회으로 전환합니다.");
            scores = findTopMatchingCandidatesLegacy(loginMember);
        }

        // 매칭 요청 이력 필터링
        scores = filterMatchingRequests(loginMember, scores);
        
        // 추천 정보를 Redis에 저장
        String candidatesKey = REDIS_CANDIDATES_KEY_PREFIX + memberId;
        
        // 후보 ID 리스트를 생성
        List<Long> candidateIds = scores.stream()
                .map(Entry::getKey)
                .collect(Collectors.toList());
        
        // Redis에 저장 - 후보 ID 리스트 저장
        redisTemplate.opsForValue().set(candidatesKey, candidateIds);
        
        // 만료 시간 설정
        redisTemplate.expire(candidatesKey, REDIS_CACHE_DURATION);

        return RecommendationResponseDto.of(candidateIds);
    }

    /**
     * 엘라스틱서치 벡터 쿼리를 이용한 추천 계산
     *
     * @param member 현재 로그인한 사용자
     * @return 점수가 높은 순서대로 정렬된 회원 ID와 점수 쌍 목록
     */
    private List<Entry<Long, Double>> findTopMatchingCandidatesWithVectorQuery(Member member) {
        Long memberId = member.getId();
        try {
            // 내 선호도 벡터 가져오기
            Map<String, Object> myPreference = elasticScriptQueryExecutor.getVectorById(PREFERENCE_INDEX, memberId);
            if (myPreference == null) {
                log.warn("내 선호도 벡터를 찾을 수 없습니다: memberId={}", memberId);
                return Collections.emptyList();
            }

            // 내 라이프스타일 벡터 가져오기
            Map<String, Object> myLifestyle = elasticScriptQueryExecutor.getVectorById(LIFESTYLE_INDEX, memberId);
            if (myLifestyle == null) {
                log.warn("내 라이프스타일 벡터를 찾을 수 없습니다: memberId={}", memberId);
                return Collections.emptyList();
            }

            // 내 선호도 벡터들 추출
            float[] preferenceWeights = convertToArray(myPreference.get(FIELD_PREFERENCE_WEIGHT));
            float[] preferredValues = convertToArray(myPreference.get(FIELD_PREFERRED_VALUES));

            // 내 라이프스타일 벡터 추출
            float[] lifestyleVector = convertToArray(myLifestyle.get(FIELD_LIFESTYLE_VECTOR));

            // 1단계: 내 선호도를 기준으로 각 사용자의 라이프스타일과 매칭 점수 계산 (나 → 상대방)
            List<Entry<Long, Double>> fromMyView = elasticScriptQueryExecutor.calculateScoresWithScript(
                    preferenceWeights,
                    preferredValues,
                    memberId,
                    member.getDormitoryType().name()
            );

            // 2단계: 다른 사용자의 선호도를 기준으로 내 라이프스타일과 매칭 점수 계산 (상대방 → 나)
            List<Entry<Long, Double>> fromTheirView = elasticScriptQueryExecutor.calculateReversedScoresWithScript(
                    lifestyleVector,
                    memberId,
                    member.getDormitoryType().name()
            );

            // 점수 합산
            Map<Long, Double> combinedScores = new HashMap<>();

            // 내 관점에서의 점수 합산
            for (Entry<Long, Double> entry : fromMyView) {
                combinedScores.put(entry.getKey(), entry.getValue());
            }

            // 상대방 관점에서의 점수 합산
            for (Entry<Long, Double> entry : fromTheirView) {
                combinedScores.merge(entry.getKey(), entry.getValue(), Double::sum);
            }

            // 점수 내림차순 정렬 후 상위 N개 반환
            return combinedScores.entrySet().stream()
                    .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(RECOMMENDATIONS_MAX_COUNT)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    /**
     * 이미 매칭 요청한 사용자 필터링
     */
    private List<Entry<Long, Double>> filterMatchingRequests(Member loginMember, List<Entry<Long, Double>> scores) {
        return scores.stream()
                .filter(entry -> {
                    Member candidateMember = memberRepository.findById(entry.getKey()).orElse(null);
                    return candidateMember != null &&
                            !matchingRequestService.isMatchingRequestAlreadyExits(loginMember, candidateMember);
                })
                .limit(RECOMMENDATIONS_MAX_COUNT)
                .collect(Collectors.toList());
    }

    /**
     * Object를 float 배열로 변환
     */
    private float[] convertToArray(Object obj) {
        if (obj instanceof List<?> list) {
            float[] result = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof Number) {
                    result[i] = ((Number) list.get(i)).floatValue();
                }
            }
            return result;
        }
        return new float[0];
    }


    /**
     * 기존 방식으로 매칭 후보 계산 (엘라스틱서치 실패 시 대체용)
     */
    private List<Entry<Long, Double>> findTopMatchingCandidatesLegacy(Member loginMember) {
        // 나의 선호 우선순위와 라이프스타일 조회
        PreferenceOrder myPreference = getPreferenceOrder(loginMember);
        Lifestyle myLifestyle = getLifestyle(loginMember);

        // 나를 제외한 전체 사용자의 라이프 스타일 조회
        List<Lifestyle> allUsersLifestyles = lifestyleRepository.findAllExcludingMember(loginMember);

        return allUsersLifestyles.stream()
                .filter(userLifestyle -> !matchingRequestService.isMatchingRequestAlreadyExits(myPreference.getMember(), userLifestyle.getMember()))
                .map(userLifestyle -> {
                    double scoreFromMyView = ScoreCalculator.calculateScoreForUser(myPreference, userLifestyle);
                    double scoreFromTheirView = preferenceOrderRepository.findByMember(userLifestyle.getMember())
                            .map(userPreference -> ScoreCalculator.calculateScoreForUser(userPreference, myLifestyle))
                            .orElse(ZERO);

                    double totalScore = scoreFromMyView + scoreFromTheirView;

                    return new AbstractMap.SimpleEntry<>(userLifestyle.getMember().getId(), totalScore);
                })
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(RECOMMENDATIONS_MAX_COUNT)
                .collect(Collectors.toList());
    }

    private void checkAlreadyMatched(Member loginMember) {
        if (loginMember.isRoommateMatched()) {
            throw new AlreadyMatchedMemberException();
        }
    }

    private PreferenceOrder getPreferenceOrder(Member member) {
        return preferenceOrderRepository.findByMember(member)
                .orElseThrow(PreferenceOrderNotExistsException::new);
    }

    private Lifestyle getLifestyle(Member loginMember) {
        return lifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(LifestyleNotExistsException::new);
    }

    @Transactional(readOnly = true)
    public RecommendationResponseDto findRecommendedCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Long memberId = loginMember.getId();

        // Redis에서 추천 정보 조회
        String candidatesKey = REDIS_CANDIDATES_KEY_PREFIX + memberId;

        // 후보 ID 리스트 조회
        Object candidatesObj = redisTemplate.opsForValue().get(candidatesKey);

        List<Long> candidateIds;
        if (candidatesObj == null) {
            candidateIds = List.of();
        } else if (candidatesObj instanceof List<?>) {
            // 타입 변환 처리
            candidateIds = ((List<?>) candidatesObj).stream()
                    .map(item -> {
                        if (item instanceof Integer) {
                            return ((Integer) item).longValue();
                        } else if (item instanceof Long) {
                            return (Long) item;
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            candidateIds = List.of();
        }

        // RecommendationResponseDto 생성
        return RecommendationResponseDto.of(candidateIds);
    }
}
