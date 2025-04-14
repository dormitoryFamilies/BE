package dormitoryfamily.doomz.global.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
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
     * 스크립트 쿼리 실행 메서드
     */
    public List<Entry<Long, Double>> executeScriptQuery(
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
                            .mustNot(mn -> mn.term(t -> t.field("member_id").value(v -> v.longValue(excludeMemberId))))))
                    ;

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

        return executeScriptQuery(LIFESTYLE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter);
    }

    /**
     * 역방향 스크립트 기반 점수 계산 쿼리 (상대방 → 나)
     * 다른 사용자의 선호도와 내 라이프스타일 간의 점수 계산
     */
    public List<Entry<Long, Double>> calculateReversedScoresWithScript(
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

        return executeScriptQuery(PREFERENCE_INDEX, scriptSource, params, excludeMemberId, dormitoryFilter);
    }
}