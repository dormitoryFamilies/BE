package dormitoryfamily.doomz.domain.roommate.recommendation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
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
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.RECOMMENDATIONS_MAX_COUNT;
import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.ZERO;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final MemberRepository memberRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final LifestyleRepository lifestyleRepository;
    private final MatchingRequestService matchingRequestService;
    private final ElasticsearchClient elasticsearchClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LIFESTYLE_INDEX = "lifestyle_vectors";
    private static final String PREFERENCE_INDEX = "preference_vectors";
    private static final String REDIS_CANDIDATES_KEY_PREFIX = "candidates:";
    private static final Duration REDIS_CACHE_DURATION = Duration.ofHours(72);

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

        return new RecommendationResponseDto(
                memberId,
                candidateIds
        );
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
            Map<String, Object> myPreference = getVectorById(PREFERENCE_INDEX, memberId);
            if (myPreference == null) {
                log.warn("내 선호도 벡터를 찾을 수 없습니다: memberId={}", memberId);
                return Collections.emptyList();
            }

            // 내 라이프스타일 벡터 가져오기
            Map<String, Object> myLifestyle = getVectorById(LIFESTYLE_INDEX, memberId);
            if (myLifestyle == null) {
                log.warn("내 라이프스타일 벡터를 찾을 수 없습니다: memberId={}", memberId);
                return Collections.emptyList();
            }

            // 내 선호도 벡터들 추출
            float[] preferenceWeights = convertToArray(myPreference.get("preference_weight"));
            float[] preferredValues = convertToArray(myPreference.get("preferred_values"));

            // 내 라이프스타일 벡터 추출
            float[] lifestyleVector = convertToArray(myLifestyle.get("lifestyle_vector"));

            // 1단계: 내 선호도를 기준으로 각 사용자의 라이프스타일과 매칭 점수 계산 (나 → 상대방)
            List<Entry<Long, Double>> fromMyView = calculateScoresWithScript(
                    preferenceWeights,
                    preferredValues,
                    memberId,
                    member.getDormitoryType().name()
            );

            // 2단계: 다른 사용자의 선호도를 기준으로 내 라이프스타일과 매칭 점수 계산 (상대방 → 나)
            List<Entry<Long, Double>> fromTheirView = calculateReversedScoresWithScript(
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
     * 스크립트 기반 점수 계산 쿼리 (나 → 상대방)
     * 내 선호도와 다른 사용자의 라이프스타일 간의 점수 계산
     */
    private List<Entry<Long, Double>> calculateScoresWithScript(
            float[] weights, float[] preferredValues, Long excludeMemberId, String dormitoryFilter) throws IOException {

        // Elasticsearch 스크립트 파라미터 설정
        Map<String, Object> params = new HashMap<>();
        params.put("weights", weights);             // 선호 가중치
        params.put("preferredValues", preferredValues); // 선호 값

        // painless 스크립트 정의
        String scriptSource = """
        double score = 0.0;
        def targetVector = params._source.lifestyle_vector;
        
        for (int i = 0; i < params.weights.length && i < targetVector.length; i++) {
            if (params.weights[i] >= 0.1) {
                double difference = Math.abs(params.preferredValues[i] - targetVector[i]);
                double attributeScore = 10 - difference;
                score += attributeScore * params.weights[i];
            }
        }
        return score;
    """;

        return executeScriptQuery(RecommendationService.LIFESTYLE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter);
    }

    /**
     * 역방향 스크립트 기반 점수 계산 쿼리 (상대방 → 나)
     * 다른 사용자의 선호도와 내 라이프스타일 간의 점수 계산
     */
    private List<Entry<Long, Double>> calculateReversedScoresWithScript(
            float[] myLifestyleVector, Long excludeMemberId, String dormitoryFilter) throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("myLifestyle", myLifestyleVector); // 내가 가진 lifestyle 벡터

        // painless 스크립트 정의
        String scriptSource = """
        double score = 0.0;
        def weights = params._source.preference_weight;
        def preferredValues = params._source.preferred_values;
        
        for (int i = 0; i < weights.length && i < params.myLifestyle.length; i++) {
            if (weights[i] >= 0.1) {
                double difference = Math.abs(preferredValues[i] - params.myLifestyle[i]);
                double attributeScore = 10 - difference;
                score += attributeScore * weights[i];
            }
        }
        return score;
    """;

        return executeScriptQuery(RecommendationService.PREFERENCE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter);
    }

    /**
     * 스크립트 쿼리 실행 메서드
     */
    private List<Entry<Long, Double>> executeScriptQuery(
            String indexName,
            String scriptSource,
            Map<String, Object> params,
            Long excludeMemberId,
            String dormitoryFilter
    ) throws IOException {

        log.info("스크립트 쿼리 실행 시작: index={}, excludeId={}, dormitory={}", 
                 indexName, excludeMemberId, dormitoryFilter);
        
        try {
            // term 대신 match 쿼리 사용
            Query filterQuery = Query.of(q -> q
                    .bool(b -> b
                            .must(mb -> mb.match(m -> m.field("dormitory").query(dormitoryFilter)))
                            .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))))
                    )
            );

            // 2. Script 객체 구성
            Script script = Script.of(s -> s
                    .inline(inline -> inline
                            .source(scriptSource)
                            .lang("painless")
                            .params(params.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            e -> JsonData.of(e.getValue())
                                    ))
                            )
                    )
            );

            // 3. SearchRequest 구성
            SearchRequest request = SearchRequest.of(r -> r
                    .index(indexName)
                    .query(q -> q
                            .scriptScore(ss -> ss
                                    .query(filterQuery)
                                    .script(script)
                            )
                    )
                    .size(RECOMMENDATIONS_MAX_COUNT * 5)  // 후보 충분히 확보
            );

            // 4. 검색 실행
            SearchResponse<Map<String, Object>> response = elasticsearchClient.search(
                    request, (Type) Map.class);

            // 5. 결과 확인 로깅
            assert response.hits().total() != null;
            log.info("스크립트 쿼리 결과: 총 히트 수={}, 실행 시간={}ms",
                    response.hits().total().value(), response.took());

            // 6. 결과 변환
            List<Entry<Long, Double>> results = response.hits().hits().stream()
                    .map(hit -> {
                        assert hit.source() != null;
                        Long candidateId = ((Number) hit.source().get("member_id")).longValue();
                        double score = hit.score();
                        return new AbstractMap.SimpleEntry<>(candidateId, score);
                    })
                    .collect(Collectors.toList());
            
            log.info("최종 변환된 결과 수: {}", results.size());
            return results;
            
        } catch (Exception e) {
            log.error("스크립트 쿼리 실행 중 오류 발생: {}", e.getMessage(), e);
            throw e;
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
     * 엘라스틱서치에서 특정 ID의 벡터 데이터 조회
     */
    private Map<String, Object> getVectorById(String indexName, Long memberId) {
        try {
            // 1. Elasticsearch 요청 객체 구성
            SearchRequest request = SearchRequest.of(r -> r
                    .index(indexName)
                    .query(q -> q
                            .term(t -> t
                                    .field("member_id")
                                    .value(v -> v.longValue(memberId))
                            )
                    )
                    .size(1) // 정확히 하나만 가져오도록 제한
            );

            // 2. 검색 요청
            SearchResponse<Map<String, Object>> response = elasticsearchClient.search(request,
                    (Type) Map.class);

            // 3. 결과 없을 경우 null 반환
            if (response.hits().hits().isEmpty()) {
                return null;
            }

            // 4. 첫 번째 결과 반환
            return response.hits().hits().get(0).source();

        } catch (IOException e) {
            return null;
        }
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
        @SuppressWarnings("unchecked")
        List<Long> candidateIds = (List<Long>) redisTemplate.opsForValue().get(candidatesKey);
        
        // 캐시에 없는 경우 빈 리스트 반환
        if (candidateIds == null) {
            return new RecommendationResponseDto(memberId, List.of());
        }
        
        // RecommendationResponseDto 생성
        return new RecommendationResponseDto(
                memberId,
                candidateIds
        );
    }
}
