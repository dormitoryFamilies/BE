package dormitoryfamily.doomz.domain.roommate.util;

import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;
import dormitoryfamily.doomz.domain.roommate.recommendation.exception.TooManyRequestException;

import java.time.Duration;
import java.time.LocalDateTime;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.RECOMMENDATION_INTERVAL_HOURS;

public class TimeIntervalCalculator {

    public static void validateRecommendationInterval(Recommendation recommendation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastRecommended = recommendation.getRecommendedAt();
        LocalDateTime nextAvailableTime = lastRecommended.plusHours(RECOMMENDATION_INTERVAL_HOURS);

        if (now.isBefore(nextAvailableTime)) {
            long remainingMinutes = Duration.between(now, nextAvailableTime).toMinutes();
            throw new TooManyRequestException(remainingMinutes);
        }
    }
}
