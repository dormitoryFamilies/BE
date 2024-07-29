package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.recommendation.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import dormitoryfamily.doomz.domain.roomate.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roomate.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roomate.exception.matching.RecommendationNotExistsException;
import dormitoryfamily.doomz.domain.roomate.repository.lifestyle.LifestyleRepository;
import dormitoryfamily.doomz.domain.roomate.repository.preferenceorder.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roomate.repository.recommendation.CandidateRepository;
import dormitoryfamily.doomz.domain.roomate.repository.recommendation.RecommendationRepository;
import dormitoryfamily.doomz.domain.roomate.util.TimeIntervalCalculator;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
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

    //todo 4. PreferenceOrder 하나의 엔티티로 변경해야 하는지 결정하기
    //todo 5. 리포지토리 테스트 코드 짜보기
    public RecommendationResponseDto findTopCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();

        //기존 매칭 추천을 조회하거나, 새로운 매칭 추천 생성
        Recommendation recommendation = getOrCreateRecommendation(loginMember);

        //나의 선호 우선순위와 라이프스타일 조회
        List<PreferenceOrder> myPreferences = getPreferenceOrders(loginMember);
        Lifestyle myLifestyle = getLifestyle(loginMember);

        //나를 제외한 전체 사용자의 라이프 스타일 조회
        List<Lifestyle> allUsersLifestyles = lifestyleRepository.findAllExcludingMember(loginMember);

        //상위 점수대 회원 산출
        List<Entry<Long, Double>> scores = findTopMatchingCandidates(myPreferences, myLifestyle, allUsersLifestyles);
        List<Candidate> candidates = createCandidates(scores, recommendation);

        //기존 후보 레코드 삭제 후 새롭게 저장
        candidateRepository.deleteAllByRecommendation(recommendation);
        candidateRepository.saveAll(candidates);

        return RecommendationResponseDto.fromEntity(recommendation, candidates);
    }

    private Recommendation getOrCreateRecommendation(Member loginMember) {
        return recommendationRepository.findByMemberId(loginMember.getId())
                .map(existingRecommendation -> {
                    //매칭 가능 시간인지 체크
                    TimeIntervalCalculator.validateRecommendationInterval(existingRecommendation);
                    existingRecommendation.updateRecommendedAt();
                    return existingRecommendation;
                }).orElseGet(() -> {
                    Recommendation newRecommendation = new Recommendation(loginMember);
                    return recommendationRepository.save(newRecommendation);
                });
    }

    private List<PreferenceOrder> getPreferenceOrders(Member loginMember) {
        List<PreferenceOrder> myPreferences = preferenceOrderRepository
                .findAllByMemberOrderByPreferenceOrderAsc(loginMember);
        if (myPreferences.isEmpty()) {
            throw new PreferenceOrderNotExistsException();
        }
        return myPreferences;
    }

    private Lifestyle getLifestyle(Member loginMember) {
        return lifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(LifestyleNotExistsException::new);
    }

    /**
     * 선호 우선순위와 라이프 스타일을 비교하여 점수를 계산해 높은 점수 순으로 사용자 id 반환하는 메소드
     *
     * @param myPreferences      나의 선호 우선순위(1 ~ 4순위)
     * @param myLifestyle        나의 라이프 스타일
     * @param allUsersLifestyles 전체 사용자의 라이프 스타일
     * @return 높은 점수 순으로 정렬된 회원 아이디 리스트
     */
    private List<Entry<Long, Double>> findTopMatchingCandidates(
            List<PreferenceOrder> myPreferences,
            Lifestyle myLifestyle,
            List<Lifestyle> allUsersLifestyles
    ) {
        return allUsersLifestyles.stream()
                .map(userLifestyle -> {

                    List<PreferenceOrder> userPreferences = preferenceOrderRepository
                            .findAllByMemberOrderByPreferenceOrderAsc(userLifestyle.getMember());

                    double scoreFromMyView = calculateScoreForUser(myPreferences, userLifestyle);
                    double scoreFromTheirView = calculateScoreForUser(userPreferences, myLifestyle);
                    double totalScore = scoreFromMyView + scoreFromTheirView;

                    return new AbstractMap.SimpleEntry<>(userLifestyle.getMember().getId(), totalScore);
                })
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(RECOMMENDATIONS_MAX_COUNT)
                .collect(Collectors.toList());
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

    public RecommendationResponseDto findRecommendedCandidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Recommendation recommendation = recommendationRepository.findByMemberId(loginMember.getId())
                .orElseThrow(RecommendationNotExistsException::new);
        List<Candidate> candidates = candidateRepository
                .findAllByRecommendationIdOrderByCandidateScoreDesc(recommendation.getId());

        return RecommendationResponseDto.fromEntity(recommendation, candidates);
    }
}
