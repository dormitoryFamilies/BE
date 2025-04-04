package dormitoryfamily.doomz.domain.roommate.recommendation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
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
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Candidate;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;
import dormitoryfamily.doomz.domain.roommate.recommendation.exception.RecommendationNotExistsException;
import dormitoryfamily.doomz.domain.roommate.recommendation.repository.CandidateRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.repository.RecommendationRepository;
import dormitoryfamily.doomz.domain.roommate.util.ScoreCalculator;
import dormitoryfamily.doomz.domain.roommate.util.TimeIntervalCalculator;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    private final RecommendationRepository recommendationRepository;
    private final CandidateRepository candidateRepository;
    private final MemberRepository memberRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final LifestyleRepository lifestyleRepository;
    private final MatchingRequestService matchingRequestService;
    private final ElasticsearchClient elasticsearchClient;

    private static final String LIFESTYLE_INDEX = "lifestyle_vectors";
    private static final String PREFERENCE_INDEX = "preference_vectors";

    public RecommendationResponseDto findTopCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadyMatched(loginMember);

        // 기존 매칭 추천을 조회하거나, 새로운 매칭 추천 생성
        Recommendation recommendation = getOrCreateRecommendation(loginMember);

        // 엘라스틱서치 벡터 쿼리 기반으로 추천 점수 계산
        List<Entry<Long, Double>> scores = findTopMatchingCandidatesWithVectorQuery(loginMember);

        // 결과가 없거나 오류가 발생한 경우 기존 방식으로 계산
        if (scores.isEmpty()) {
            log.warn("엘라스틱서치 벡터 쿼리 추천 결과가 없거나 처리 중 오류 발생, 데이터베이스 조회으로 전환합니다.");
            scores = findTopMatchingCandidatesLegacy(loginMember);
        }

        // 매칭 요청 이력 필터링
        scores = filterMatchingRequests(loginMember, scores);

        List<Candidate> candidates = createCandidates(scores, recommendation);

        // 기존 후보 레코드 삭제 후 새롭게 저장
        candidateRepository.deleteAllByRecommendation(recommendation);
        candidateRepository.saveAll(candidates);

        return RecommendationResponseDto.fromEntity(recommendation, candidates);
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
                    LIFESTYLE_INDEX,
                    preferenceWeights,
                    preferredValues,
                    memberId,
                    member.getDormitoryType().name()
            );

            // 2단계: 다른 사용자의 선호도를 기준으로 내 라이프스타일과 매칭 점수 계산 (상대방 → 나)
            List<Entry<Long, Double>> fromTheirView = calculateReversedScoresWithScript(
                    PREFERENCE_INDEX,
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
            String indexName, float[] weights, float[] preferredValues, Long excludeMemberId, String dormitoryFilter) throws IOException {

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

        return executeScriptQuery(indexName, scriptSource, params, excludeMemberId, dormitoryFilter);
    }

    /**
     * 역방향 스크립트 기반 점수 계산 쿼리 (상대방 → 나)
     * 다른 사용자의 선호도와 내 라이프스타일 간의 점수 계산
     */
    private List<Entry<Long, Double>> calculateReversedScoresWithScript(
            String indexName, float[] myLifestyleVector, Long excludeMemberId, String dormitoryFilter) throws IOException {

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

        return executeScriptQuery(indexName, scriptSource, params, excludeMemberId, dormitoryFilter);
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

            // 디버깅용 로그 - 요청 내용 확인
            log.debug("Elasticsearch 검색 요청: {}", request);

            // 4. 검색 실행
            SearchResponse<Map<String, Object>> response = elasticsearchClient.search(
                    request, (Type) Map.class);

            // 5. 결과 확인 로깅
            log.info("스크립트 쿼리 결과: 총 히트 수={}, 실행 시간={}ms", 
                    response.hits().total().value(), response.took());
            
            // 결과가 없는 경우 로그
            if (response.hits().hits().isEmpty()) {
                log.warn("스크립트 쿼리 결과 없음: index={}, dormitory={}", indexName, dormitoryFilter);
                
                // 테스트를 위해 dormitory 필터 없이 다시 한번 실행해보기
                Query testQuery = Query.of(q -> q
                        .bool(b -> b
                                .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))))
                        )
                );
                
                SearchRequest testRequest = SearchRequest.of(r -> r
                        .index(indexName)
                        .query(testQuery)
                        .size(5));
                
                SearchResponse<Map<String, Object>> testResponse = elasticsearchClient.search(
                        testRequest, (Type) Map.class);
                
                log.info("필터 없는 테스트 쿼리 결과: 히트 수={}", testResponse.hits().total().value());
                if (!testResponse.hits().hits().isEmpty()) {
                    log.info("첫 번째 히트 dormitory 값: {}", 
                             testResponse.hits().hits().get(0).source().get("dormitory"));
                }
            }

            // 6. 결과 변환
            List<Entry<Long, Double>> results = response.hits().hits().stream()
                    .map(hit -> {
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
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
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
                log.warn("엘라스틱서치에서 member_id={} 에 해당하는 벡터가 없습니다. index={}", memberId, indexName);
                return null;
            }

            // 4. 첫 번째 결과 반환
            return response.hits().hits().get(0).source();

        } catch (IOException e) {
            log.error("엘라스틱서치 벡터 조회 실패: index={}, member_id={}", indexName, memberId, e);
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

    private Recommendation getOrCreateRecommendation(Member loginMember) {
        return recommendationRepository.findByMemberId(loginMember.getId())
                .map(existingRecommendation -> {
                    // 매칭 가능 시간인지 체크
                    TimeIntervalCalculator.validateRecommendationInterval(existingRecommendation);
                    existingRecommendation.updateRecommendedAt();
                    return existingRecommendation;
                }).orElseGet(() -> {
                    Recommendation newRecommendation = Recommendation.builder().member(loginMember).build();
                    return recommendationRepository.save(newRecommendation);
                });
    }

    private PreferenceOrder getPreferenceOrder(Member member) {
        return preferenceOrderRepository.findByMember(member)
                .orElseThrow(PreferenceOrderNotExistsException::new);
    }

    private Lifestyle getLifestyle(Member loginMember) {
        return lifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(LifestyleNotExistsException::new);
    }

    /**
     * @param scores         회원 아이디와 점수 리스트
     * @param recommendation Recommendation 레코드
     * @return 생성된 Candidate 레코드 리스트
     */
    private List<Candidate> createCandidates(List<Entry<Long, Double>> scores, Recommendation recommendation) {
        return scores.stream()
                .map(entry -> {
                    Member candidate = memberRepository.findById(entry.getKey())
                            .orElseThrow(MemberNotExistsException::new);
                    return Candidate.builder()
                            .recommendation(recommendation)
                            .candidateMember(candidate)
                            .candidateScore(entry.getValue())
                            .build();
                }).toList();
    }

    @Transactional(readOnly = true)
    public RecommendationResponseDto findRecommendedCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Recommendation recommendation = recommendationRepository.findByMemberId(loginMember.getId())
                .orElseThrow(RecommendationNotExistsException::new);

        List<Candidate> candidates = recommendation.getCandidates().stream()
                .sorted(Comparator.comparing(Candidate::getCandidateScore).reversed())
                .toList();

        return RecommendationResponseDto.fromEntity(recommendation, candidates);
    }
}