package dormitoryfamily.doomz.domain.roommate.recommendation.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.roommate.matching.exception.AlreadyMatchedMemberException;
import dormitoryfamily.doomz.domain.roommate.matching.repository.MatchingRequestRepository;
import dormitoryfamily.doomz.domain.roommate.matching.service.MatchingRequestService;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.dto.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roommate.util.ScoreCalculator;
import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeoutException;
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
    private final MatchingRequestRepository matchingRequestRepository;

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

            log.info("벡터 쿼리 시작 - memberId: {}", memberId);
            log.info("선호도 가중치: {}", Arrays.toString(preferenceWeights));
            log.info("선호 값: {}", Arrays.toString(preferredValues));
            log.info("라이프스타일 벡터: {}", Arrays.toString(lifestyleVector));

            String dormitoryType = member.getDormitoryType().name();

            // 0단계: kNN으로 후보 추출
            List<Long> candidates;
            try {
                candidates = elasticScriptQueryExecutor.findTopKCandidates(
                        preferredValues,  // 내 선호 값으로 유사한 사람 찾기
                        dormitoryType,
                        memberId,
                        ANN_CANDIDATE_POOL_SIZE
                );
                log.info("kNN으로 추출된 후보: {}명", candidates.size());
            } catch (Exception e) {
                log.error("kNN 후보 추출 실패, 전체 대상으로 진행", e);
                candidates = null;  // null이면 executeScriptQuery에서 전체 대상
            }

            // 최종 후보 리스트
            final List<Long> finalCandidates = candidates;

            // 병렬 처리: 두 방향의 점수 계산을 동시에 실행
            CompletableFuture<List<Entry<Long, Double>>> fromMyViewFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    // 1단계: 내 선호도를 기준으로 각 사용자의 라이프스타일과 매칭 점수 계산 (나 → 상대방)
                    return elasticScriptQueryExecutor.calculateScoresWithScript(
                            preferenceWeights,
                            preferredValues,
                            memberId,
                            dormitoryType,
                            finalCandidates
                    );
                } catch (Exception e) {
                    log.error("나 → 상대방 점수 계산 중 오류 발생", e);
                    return Collections.emptyList();
                }
            });

            CompletableFuture<List<Entry<Long, Double>>> fromTheirViewFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    // 2단계: 다른 사용자의 선호도를 기준으로 내 라이프스타일과 매칭 점수 계산 (상대방 → 나)
                    return elasticScriptQueryExecutor.calculateReversedScoresWithScript(
                            lifestyleVector,
                            memberId,
                            dormitoryType,
                            finalCandidates
                    );
                } catch (Exception e) {
                    log.error("상대방 → 나 점수 계산 중 오류 발생", e);
                    return Collections.emptyList();
                }
            });

            // 두 작업이 모두 완료될 때까지 대기
            CompletableFuture<Void> allTasks = CompletableFuture.allOf(fromMyViewFuture, fromTheirViewFuture);

            // 타임아웃 설정 (예: 30초)
            allTasks.get(30, TimeUnit.SECONDS);

            // 결과 가져오기
            List<Entry<Long, Double>> fromMyView = fromMyViewFuture.get();
            List<Entry<Long, Double>> fromTheirView = fromTheirViewFuture.get();

            log.info("병렬 처리 완료 - 나→상대방: {}개, 상대방→나: {}개", fromMyView.size(), fromTheirView.size());

            // 3단계: 두 점수를 합산하여 최종 추천 점수 계산
            Map<Long, Double> combinedScores = new HashMap<>();

            // 나 → 상대방 점수 추가
            for (Entry<Long, Double> entry : fromMyView) {
                combinedScores.put(entry.getKey(), entry.getValue());
            }

            // 상대방 → 나 점수 추가 (합산)
            for (Entry<Long, Double> entry : fromTheirView) {
                combinedScores.merge(entry.getKey(), entry.getValue(), Double::sum);
            }

            log.info("최종 합산 완료 - 총 {}명의 후보", combinedScores.size());

            return combinedScores.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // 내림차순
                    .toList();

        } catch (TimeoutException e) {
            log.error("벡터 쿼리 처리 시간 초과 (30초)", e);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("벡터 쿼리 기반 추천 계산 중 오류 발생", e);
            return Collections.emptyList();
        }
    }

    /**
     * 이미 매칭 요청한 사용자 필터링
     */
    private List<Entry<Long, Double>> filterMatchingRequests(Member loginMember, List<Entry<Long, Double>> scores) {

        // 모든 후보 ID 수집
        List<Long> candidateIds = scores.stream()
                .map(Entry::getKey)
                .collect(Collectors.toList());

        // 존재하는 Member들을 한 번에 조회
        List<Member> existingMembers = memberRepository.findAllById(candidateIds);
        Set<Long> existingMemberIds = existingMembers.stream()
                .map(Member::getId)
                .collect(Collectors.toSet());

        // 매칭 요청 이력을 한 번에 조회
        List<MatchingRequest> existingRequests = matchingRequestRepository
                .findAllByMemberAndCandidateIds(loginMember, candidateIds);

        // 매칭 요청이 있는 멤버 ID들을 Set으로 변환
        Set<Long> requestedMemberIds = existingRequests.stream()
                .map(request -> {
                    // sender가 loginMember면 receiver의 ID, 아니면 sender의 ID
                    return request.getSender().getId().equals(loginMember.getId())
                            ? request.getReceiver().getId()
                            : request.getSender().getId();
                })
                .collect(Collectors.toSet());

        List<Entry<Long, Double>> filteredScores = scores.stream()
                .filter(entry -> {
                    Long candidateId = entry.getKey();
                    boolean isValid = existingMemberIds.contains(candidateId) &&  // 존재하는 멤버인가?
                            !requestedMemberIds.contains(candidateId);   // 매칭 요청 없는가?

                    if (!isValid) {
                        log.info("후보 제외: memberId={}, reason={}",
                                candidateId,
                                !existingMemberIds.contains(candidateId) ? "존재하지 않는 사용자" : "이미 매칭 요청 있음");
                    }

                    return isValid;
                })
                .limit(RECOMMENDATIONS_MAX_COUNT)
                .collect(Collectors.toList());

        return filteredScores;
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

        // 1. 모든 후보 Member 객체 수집
        List<Member> candidateMembers = allUsersLifestyles.stream()
                .map(Lifestyle::getMember)
                .collect(Collectors.toList());

        //2. 모든 후보의 PreferenceOrder를 한 번에 조회
        List<PreferenceOrder> allPreferences = preferenceOrderRepository.findAllByMemberIn(candidateMembers);

        // 3. Map으로 변환
        Map<Long, PreferenceOrder> preferenceMap = allPreferences.stream()
                .collect(Collectors.toMap(
                        p -> p.getMember().getId(),
                        p -> p
                ));

        // 4. 해당 멤버가 포함된 모든 매칭 요청 이력 조회
        List<MatchingRequest> existingRequests = matchingRequestRepository
                .findAllByMemberAndCandidates(loginMember, candidateMembers);

        // 5. 매칭 요청이 있는 멤버 ID들을 Set으로 변환
        Set<Long> requestedMemberIds = existingRequests.stream()
                .map(request -> {
                    return request.getSender().getId().equals(loginMember.getId())
                            ? request.getReceiver().getId()
                            : request.getSender().getId();
                })
                .collect(Collectors.toSet());

        // 6. Stream으로 점수 계산
        return allUsersLifestyles.stream()
                .filter(userLifestyle -> {
                    // 이미 매칭 요청한 사용자는 제외
                    return !requestedMemberIds.contains(userLifestyle.getMember().getId());
                })
                .map(userLifestyle -> {
                    // 내가 상대방을 보는 점수
                    double scoreFromMyView = ScoreCalculator.calculateScoreForUser(myPreference, userLifestyle);

                    // 상대방이 나를 보는 점수
                    double scoreFromTheirView = Optional.ofNullable(
                            // map에서 상대방의 선호도 가져오기
                                    preferenceMap.get(userLifestyle.getMember().getId())
                            )
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
