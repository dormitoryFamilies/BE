package dormitoryfamily.doomz.domain.roomate.util;

import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import dormitoryfamily.doomz.domain.roomate.exception.matching.TooManyRequestException;

import java.time.Duration;
import java.time.LocalDateTime;

import static dormitoryfamily.doomz.domain.roomate.util.RoommateProperties.RECOMMENDATION_INTERVAL_HOURS;

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
