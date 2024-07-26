package dormitoryfamily.doomz.domain.roomate.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomate.dto.recommendation.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleAttribute;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
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

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final CandidateRepository candidateRepository;
    private final MemberRepository memberRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final LifestyleRepository lifestyleRepository;

    /*
        스토어드 프로시저를 사용하려고 했지만 enum 타입
     */
    public RecommendationResponseDto findTop5Candidates(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();

        // 로그인 사용자의 선호 우선순위를 우선순위 순으로 정렬한 뒤 조회(1~4순위)
        List<PreferenceOrder> targetPreferenceOrders = preferenceOrderRepository.findAllByMemberOrderByPreferenceOrderAsc(loginMember);

        List<Lifestyle> allLifeStyles = lifestyleRepository.findAll();

        List<Map.Entry<Long, Integer>> scores = allLifeStyles.stream()
                .map(lifestyle -> {
                    int score = calculateScore(targetPreferenceOrders, lifestyle);
                    return new AbstractMap.SimpleEntry<>(lifestyle.getMember().getId(), score);
                })
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toList());

        System.out.println("scores = " + scores);

        // 점수 내는 알고리즘 만들기

        // Recommendation 엔티티 생성 및 저장

        // DB에서 preferenceOrder 찾아서 점수 내기

        // TOP 5 만들어서 Candidate 생성 및 저장

        // DTO로 반환

        Member m1 = memberRepository.findById(655L).get();
        Member m2 = memberRepository.findById(656L).get();
        Member m3 = memberRepository.findById(657L).get();
        Member m4 = memberRepository.findById(658L).get();
        Member m5 = memberRepository.findById(659L).get();

        Recommendation recommendation = new Recommendation(loginMember);
        Candidate c1 = Candidate.builder()
                .recommendation(recommendation)
                .candidateMember(m1)
                .candidateScore(10).build();
        Candidate c2 = Candidate.builder()
                .recommendation(recommendation)
                .candidateMember(m2)
                .candidateScore(16).build();
        Candidate c3 = Candidate.builder()
                .recommendation(recommendation)
                .candidateMember(m3)
                .candidateScore(20).build();
        Candidate c4 = Candidate.builder()
                .recommendation(recommendation)
                .candidateMember(m4)
                .candidateScore(3).build();
        Candidate c5 = Candidate.builder()
                .recommendation(recommendation)
                .candidateMember(m5)
                .candidateScore(9).build();

        List<Candidate> candidateList = List.of(c1, c2, c3, c4, c5);

        candidateRepository.save(c1);
        candidateRepository.save(c2);
        candidateRepository.save(c3);
        candidateRepository.save(c4);
        candidateRepository.save(c5);

        recommendationRepository.save(recommendation);

        return RecommendationResponseDto.fromEntity(recommendation, candidateList);
    }

    private int calculateScore(List<PreferenceOrder> PreferenceOrders, Lifestyle lifestyle) {
        int score = 0;

        for (PreferenceOrder preferenceOrder : PreferenceOrders) {
            score += calculateIndividualScore(preferenceOrder, lifestyle);
        }

        return score;
    }

    private int calculateIndividualScore(PreferenceOrder preferenceOrder, Lifestyle lifestyle) {
        LifestyleType preferredLifestyleType = preferenceOrder.getLifestyleType();

        Enum<?> preferredLifestyleDetail = preferenceOrder.getLifestyleDetail();
        Enum<?> comparedLifestyle = getComparedLifestyle(preferredLifestyleType, lifestyle);

        return 9 - Math.abs(((LifestyleAttribute) preferredLifestyleDetail).getIndex() - ((LifestyleAttribute) comparedLifestyle).getIndex());
    }

    private Enum<?> getComparedLifestyle(LifestyleType preferredLifestyle, Lifestyle targetLifestyle) {
        switch (preferredLifestyle) {
            case SLEEP_TIME:
                return targetLifestyle.getSleepTimeType();
            case WAKE_UP_TIME:
                return targetLifestyle.getWakeUpTimeType();
            case SMOKING:
                return targetLifestyle.getSmokingType();
            case SLEEPING_HABIT:
                return targetLifestyle.getSleepingHabitType();
            case SLEEPING_SENSITIVITY:
                return targetLifestyle.getSleepingSensitivityType();
            case DRINKING_FREQUENCY:
                return targetLifestyle.getDrinkingFrequencyType();
            case CLEANING_FREQUENCY:
                return targetLifestyle.getCleaningFrequencyType();
            case HEAT_TOLERANCE:
                return targetLifestyle.getHeatToleranceType();
            case COLD_TOLERANCE:
                return targetLifestyle.getColdToleranceType();
            case PERFUME_USAGE:
                return targetLifestyle.getPerfumeUsageType();
            case EXAM_PREPARATION:
                return targetLifestyle.getExamPreparationType();
            default:
                return null;
        }
    }
}
