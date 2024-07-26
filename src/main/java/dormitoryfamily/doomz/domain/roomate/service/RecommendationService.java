package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.recommendation.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import dormitoryfamily.doomz.domain.roomate.repository.lifestyle.LifestyleRepository;
import dormitoryfamily.doomz.domain.roomate.repository.preferenceorder.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roomate.repository.recommendation.CandidateRepository;
import dormitoryfamily.doomz.domain.roomate.repository.recommendation.RecommendationRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.roomate.util.RoommateProperties.RECOMMENDATIONS_MAX_COUNT;
import static dormitoryfamily.doomz.domain.roomate.util.ScoreCalculator.calculateScoreForUser;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final CandidateRepository candidateRepository;
    private final MemberRepository memberRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final LifestyleRepository lifestyleRepository;

    //todo 1. 상대방의 선호 우선순위와 내 라이프스타일 비교
    //todo 4. PreferenceOrder 하나의 엔티티로 변경해야 하는지 결정하기
    //todo 5. 리포지토리 테스트 코드 짜보기
    //todo 6. 예외처리
    // - 하루 횟수 초과시 : 예외처리 하고 나면, 다시 등록할 경우 기존거 삭제하고 다시 등록하는 로직 수행하기
    // - 프로필 등록, 선호 우선순위 등록을 안했을 시
    // - 아무도 라이프 스타일 등록 안했다면 어떻게 되나?
    // - 2~3명만 해당되면?
    //todo 7. 하루에 한 번만 가능
    public RecommendationResponseDto findTopCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();

        List<PreferenceOrder> preferenceOrders =
                preferenceOrderRepository.findAllByMemberOrderByPreferenceOrderAsc(loginMember);
        List<Lifestyle> lifestyles = lifestyleRepository.findAll();

        List<Map.Entry<Long, Double>> scores = findTopMatchingCandidates(lifestyles, preferenceOrders);

        Recommendation recommendation = new Recommendation(loginMember);
        List<Candidate> candidates = createCandidates(scores, recommendation);
        recommendationRepository.save(recommendation);
        candidateRepository.saveAll(candidates);

        return RecommendationResponseDto.fromEntity(recommendation, candidates);
    }

    /**
     * 선호 우선순위와 라이프 스타일을 비교하여 점수를 계산해 높은 점수 순으로 사용자 id 반환하는 메소드
     *
     * @param lifestyles       전체 사용자의 라이프 스타일
     * @param preferenceOrders 비교 대상의 선호 우선순위(1 ~ 4순위)
     * @return 높은 점수 순으로 정렬된 회원 아이디 리스트
     */
    private static List<Map.Entry<Long, Double>> findTopMatchingCandidates(
            List<Lifestyle> lifestyles,
            List<PreferenceOrder> preferenceOrders
    ) {
        return lifestyles.stream()
                .map(lifestyle -> {
                    double score = calculateScoreForUser(preferenceOrders, lifestyle);
                    return new AbstractMap.SimpleEntry<>(lifestyle.getMember().getId(), score);
                })
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(RECOMMENDATIONS_MAX_COUNT)
                .collect(Collectors.toList());
    }

    /**
     * @param scores         회원 아이디와 점수 리스트
     * @param recommendation Recommendation 레코드
     * @return 생성된 Candidate 레코드 리스트
     */
    private List<Candidate> createCandidates(List<Map.Entry<Long, Double>> scores, Recommendation recommendation) {
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
}
