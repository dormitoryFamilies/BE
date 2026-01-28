package dormitoryfamily.doomz.domain.roommate.recommendation.util;

import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.*;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;

import java.util.ArrayList;
import java.util.List;

public class LifestyleComparisonBuilder {

    private LifestyleComparisonBuilder() {
        throw new UnsupportedOperationException("유틸리티 클래스입니다.");
    }

    public static String buildComparisonText(
            Lifestyle userLifestyle,
            Lifestyle candidateLifestyle,
            PreferenceOrder userPreference
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("### 나의 라이프스타일\n");
        sb.append(formatLifestyle(userLifestyle));

        sb.append("\n### 상대방의 라이프스타일\n");
        sb.append(formatLifestyle(candidateLifestyle));

        sb.append("\n### 나의 우선순위\n");
        sb.append(formatPreferences(userPreference));

        return sb.toString();
    }

    private static String formatLifestyle(Lifestyle lifestyle) {
        StringBuilder sb = new StringBuilder();

        sb.append("- 취침 시간: ").append(lifestyle.getSleepTimeType().getDescription()).append("\n");
        sb.append("- 기상 시간: ").append(lifestyle.getWakeUpTimeType().getDescription()).append("\n");
        sb.append("- 잠버릇: ").append(lifestyle.getSleepingHabitType().getDescription()).append("\n");
        sb.append("- 수면 민감도: ").append(lifestyle.getSleepingSensitivityType().getDescription()).append("\n");
        sb.append("- 흡연: ").append(lifestyle.getSmokingType().getDescription()).append("\n");
        sb.append("- 음주 빈도: ").append(lifestyle.getDrinkingFrequencyType().getDescription()).append("\n");
        sb.append("- 청소 주기: ").append(lifestyle.getCleaningFrequencyType().getDescription()).append("\n");
        sb.append("- 더위 민감도: ").append(lifestyle.getHeatToleranceType().getDescription()).append("\n");
        sb.append("- 추위 민감도: ").append(lifestyle.getColdToleranceType().getDescription()).append("\n");
        sb.append("- 향수 사용: ").append(lifestyle.getPerfumeUsageType().getDescription()).append("\n");
        sb.append("- 시험 기간 습관: ").append(lifestyle.getExamPreparationType().getDescription()).append("\n");

        if (lifestyle.getShowerTimeType() != null) {
            sb.append("- 샤워 시간대: ").append(lifestyle.getShowerTimeType().getDescription()).append("\n");
        }
        if (lifestyle.getMbtiType() != null) {
            sb.append("- MBTI: ").append(lifestyle.getMbtiType().name()).append("\n");
        }

        return sb.toString();
    }

    private static String formatPreferences(PreferenceOrder preference) {
        StringBuilder sb = new StringBuilder();
        sb.append("1순위: ").append(getPreferenceDescription(preference.getFirstPreferenceOrder())).append("\n");
        sb.append("2순위: ").append(getPreferenceDescription(preference.getSecondPreferenceOrder())).append("\n");
        sb.append("3순위: ").append(getPreferenceDescription(preference.getThirdPreferenceOrder())).append("\n");
        sb.append("4순위: ").append(getPreferenceDescription(preference.getFourthPreferenceOrder())).append("\n");
        return sb.toString();
    }

    private static String getPreferenceDescription(Enum<?> preference) {
        if (preference instanceof LifestyleAttribute attr) {
            return attr.getDescription();
        }
        return preference.name();
    }

    public static List<String> findMatchingAttributes(Lifestyle userLifestyle, Lifestyle candidateLifestyle) {
        List<String> matches = new ArrayList<>();

        if (userLifestyle.getSmokingType() == candidateLifestyle.getSmokingType()) {
            if (userLifestyle.getSmokingType() == SmokingType.NON_SMOKER) {
                matches.add("비흡연자");
            } else {
                matches.add("흡연자");
            }
        }

        if (userLifestyle.getCleaningFrequencyType() == candidateLifestyle.getCleaningFrequencyType()) {
            matches.add("청소 습관(" + userLifestyle.getCleaningFrequencyType().getDescription() + ")");
        }

        int sleepTimeDiff = Math.abs(
                userLifestyle.getSleepTimeType().getIndex() - candidateLifestyle.getSleepTimeType().getIndex()
        );
        if (sleepTimeDiff <= 1) {
            matches.add("비슷한 취침 시간");
        }

        int wakeUpTimeDiff = Math.abs(
                userLifestyle.getWakeUpTimeType().getIndex() - candidateLifestyle.getWakeUpTimeType().getIndex()
        );
        if (wakeUpTimeDiff <= 1) {
            matches.add("비슷한 기상 시간");
        }

        if (userLifestyle.getSleepingSensitivityType() == candidateLifestyle.getSleepingSensitivityType()) {
            matches.add("수면 민감도(" + userLifestyle.getSleepingSensitivityType().getDescription() + ")");
        }

        if (userLifestyle.getDrinkingFrequencyType() == candidateLifestyle.getDrinkingFrequencyType()) {
            matches.add("음주 빈도(" + userLifestyle.getDrinkingFrequencyType().getDescription() + ")");
        }

        if (userLifestyle.getHeatToleranceType() == candidateLifestyle.getHeatToleranceType()) {
            matches.add("더위 민감도");
        }

        if (userLifestyle.getColdToleranceType() == candidateLifestyle.getColdToleranceType()) {
            matches.add("추위 민감도");
        }

        return matches;
    }

    public static String buildFallbackExplanation(Lifestyle userLifestyle, Lifestyle candidateLifestyle) {
        List<String> matches = findMatchingAttributes(userLifestyle, candidateLifestyle);

        if (matches.isEmpty()) {
            return "다양한 라이프스타일 항목을 종합적으로 고려했을 때 좋은 룸메이트가 될 수 있어요!";
        }

        if (matches.size() == 1) {
            return "두 분 모두 " + matches.get(0) + " 성향이 비슷해요! 편안한 기숙사 생활이 기대됩니다.";
        }

        String matchList = String.join(", ", matches.subList(0, Math.min(matches.size(), 3)));
        return "두 분 모두 " + matchList + " 성향이 비슷해요! 편안한 기숙사 생활이 기대됩니다.";
    }
}
