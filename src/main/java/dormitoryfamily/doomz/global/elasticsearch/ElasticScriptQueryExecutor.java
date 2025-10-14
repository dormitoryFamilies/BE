package dormitoryfamily.doomz.global.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleAttribute;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleType;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.*;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticScriptQueryExecutor {

    private final ElasticsearchClient elasticsearchClient;

    /**
     * 엘라스틱서치에 라이프스타일 벡터 저장
     */
    public void indexLifestyleVector(Member member, Lifestyle lifestyle) {
        try {
            float[] vector = convertToVector(lifestyle);

            Map<String, Object> document = new HashMap<>();
            document.put("member_id", member.getId());
            document.put(FIELD_LIFESTYLE_VECTOR, vector);
            document.put("dormitory", member.getDormitoryType().name());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(LIFESTYLE_INDEX)
                    .id(member.getId().toString())
                    .document(document)
            );

            IndexResponse response = elasticsearchClient.index(request);

            if (response.result() != Result.Created && response.result() != Result.Updated) {
                System.err.println(" 저장은 됐지만 예외적인 상태: " + response.result());
            }

        } catch (IOException e) {
            System.err.println(" Elasticsearch 저장 실패: " + e.getMessage());
            throw new RuntimeException("엘라스틱서치 저장 중 오류 발생", e);
        }
    }

    private float[] convertToVector(Lifestyle lifestyle) {
        float[] vector = new float[11];
        vector[0] = lifestyle.getSleepTimeType().getIndex();
        vector[1] = lifestyle.getWakeUpTimeType().getIndex();
        vector[2] = lifestyle.getSleepingHabitType().getIndex();
        vector[3] = lifestyle.getSleepingSensitivityType().getIndex();
        vector[4] = lifestyle.getSmokingType().getIndex();
        vector[5] = lifestyle.getDrinkingFrequencyType().getIndex();
        vector[6] = lifestyle.getCleaningFrequencyType().getIndex();
        vector[7] = lifestyle.getHeatToleranceType().getIndex();
        vector[8] = lifestyle.getColdToleranceType().getIndex();
        vector[9] = lifestyle.getPerfumeUsageType().getIndex();
        vector[10] = lifestyle.getExamPreparationType().getIndex();
        return vector;
    }

    public void indexPreferenceVector(Long memberId, PreferenceOrder order, Member member) {
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
            document.put(FIELD_PREFERENCE_WEIGHT, weightVector);
            document.put(FIELD_PREFERRED_VALUES, preferredValues);
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

    /**
     * 엘라스틱서치에서 특정 ID의 벡터 데이터 조회
     */
    public Map<String, Object> getVectorById(String indexName, Long memberId) {
        try {
            // Elasticsearch 요청 객체 구성
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

            // 검색 요청
            SearchResponse<Map<String, Object>> response = elasticsearchClient.search(request,
                    (Type) Map.class);

            // 결과 없을 경우 null 반환
            if (response.hits().hits().isEmpty()) {
                return null;
            }

            // 첫 번째 결과 반환
            return response.hits().hits().get(0).source();

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * kNN으로 Top-K 후보 빠르게 추출
     */
    public List<Long> findTopKCandidates(
            float[] queryVector,
            String dormitoryFilter,
            Long excludeMemberId,
            int k
    ) throws IOException {
        try {
            // float[] -> List<Float> 변환
            List<Float> queryVectorList = new ArrayList<>();
            for (float v : queryVector) {
                queryVectorList.add(v);
            }

            SearchRequest request = SearchRequest.of(r -> r
                    .index(LIFESTYLE_INDEX)
                    .knn(knn -> knn
                            .field(FIELD_LIFESTYLE_VECTOR)
                            .queryVector(queryVectorList)
                            .k(k)
                            .numCandidates(k * 3)
                            .filter(f -> f
                                    .bool(b -> b
                                            .must(mb -> mb.match(m -> m.field("dormitory").query(dormitoryFilter)))
                                            .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))))
                                    )
                            )
                    )
            );

            SearchResponse<Map<String, Object>> response =
                    elasticsearchClient.search(request, (Type) Map.class);

            List<Long> candidateIds = response.hits().hits().stream()
                    .map(hit -> ((Number) hit.source().get("member_id")).longValue())
                    .collect(Collectors.toList());

            log.info("kNN 후보 추출 완료: {}명 (요청: {}명)", candidateIds.size(), k);
            return candidateIds;

        } catch (Exception e) {
            log.error("kNN 후보 추출 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 스크립트 쿼리 실행 메서드
     */
    public List<Entry<Long, Double>> executeScriptQuery(
            String indexName,
            String scriptSource,
            Map<String, Object> params,
            Long excludeMemberId,
            String dormitoryFilter,
            List<Long> candidateIds
    ) throws IOException {

        log.info("스크립트 쿼리 실행 시작: index={}, excludeId={}, dormitory={}",
                indexName, excludeMemberId, dormitoryFilter);

        try {
            // 후보 ID 필터 추가
            Query filterQuery = Query.of(q -> q
                    .bool(b -> {
                        var boolBuilder = b
                                .must(mb -> mb.match(m -> m.field("dormitory").query(dormitoryFilter)))
                                .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))));

                        // 후보 ID가 있으면 필터 추가
                        if (candidateIds != null && !candidateIds.isEmpty()) {
                            boolBuilder.must(mb -> mb.terms(t -> t
                                    .field("member_id")
                                    .terms(terms -> terms.value(
                                            candidateIds.stream()
                                                    .map(id -> co.elastic.clients.elasticsearch._types.FieldValue.of(id))
                                                    .collect(Collectors.toList())
                                    ))
                            ));
                        }

                        return boolBuilder;
                    })
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
     * 스크립트 기반 점수 계산 쿼리 (나 → 상대방)
     * 내 선호도와 다른 사용자의 라이프스타일 간의 점수 계산
     */
    public List<Entry<Long, Double>> calculateScoresWithScript(
            float[] weights, float[] preferredValues, Long excludeMemberId, String dormitoryFilter, List<Long> candidateIds) throws IOException {

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

        return executeScriptQuery(LIFESTYLE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter, candidateIds);
    }

    /**
     * 역방향 스크립트 기반 점수 계산 쿼리 (상대방 → 나)
     * 다른 사용자의 선호도와 내 라이프스타일 간의 점수 계산
     */
    public List<Entry<Long, Double>> calculateReversedScoresWithScript(
            float[] myLifestyleVector, Long excludeMemberId, String dormitoryFilter, List<Long> candidateIds) throws IOException {

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

        return executeScriptQuery(PREFERENCE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter, candidateIds);
    }

    /**
     * kNN + Rescore 방식 (1회 네트워크 왕복)
     * kNN으로 후보를 추출하고, 동일 쿼리 내에서 rescore로 script 기반 재점수 계산
     */
    public List<Entry<Long, Double>> findTopKWithScript(
            float[] queryVector,
            float[] weights,
            float[] preferredValues,
            String dormitoryFilter,
            Long excludeMemberId,
            int k
    ) throws IOException {

        List<Float> queryVectorList = new ArrayList<>();
        for (float v : queryVector) {
            queryVectorList.add(v);
        }

        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("weights", weights);
        scriptParams.put("preferredValues", preferredValues);

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

        // Script 객체 구성
        Script script = Script.of(s -> s
                .inline(inline -> inline
                        .source(scriptSource)
                        .lang("painless")
                        .params(scriptParams.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        e -> JsonData.of(e.getValue())
                                ))
                        )
                )
        );

        int windowSize = Math.min(k * 2, 100); // rescore 대상 수

        SearchRequest request = SearchRequest.of(r -> r
                .index(LIFESTYLE_INDEX)
                .knn(knn -> knn
                        .field(FIELD_LIFESTYLE_VECTOR)
                        .queryVector(queryVectorList)
                        .k(windowSize)
                        .numCandidates(windowSize * 3)
                        .filter(f -> f
                                .bool(b -> b
                                        .must(mb -> mb.match(m -> m.field("dormitory").query(dormitoryFilter)))
                                        .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))))
                                )
                        )
                )
                .rescore(rs -> rs
                        .windowSize(windowSize)
                        .query(rsq -> rsq
                                .query(rq -> rq
                                        .scriptScore(ss -> ss
                                                .query(qb -> qb.matchAll(ma -> ma))
                                                .script(script)
                                        )
                                )
                                .queryWeight(1.0)    // kNN 원본 점수 가중치
                                .rescoreQueryWeight(1.0)  // rescore 점수 가중치
                        )
                )
                .size(k)
        );

        SearchResponse<Map<String, Object>> response =
                elasticsearchClient.search(request, (Type) Map.class);

        log.info("kNN + Rescore 통합 쿼리 완료: {}개 결과, 실행 시간 {}ms",
                response.hits().hits().size(), response.took());

        return response.hits().hits().stream()
                .map(hit -> new AbstractMap.SimpleEntry<>(
                        ((Number) hit.source().get("member_id")).longValue(),
                        hit.score()
                ))
                .collect(Collectors.toList());
    }
}