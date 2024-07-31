package dormitoryfamily.doomz.domain.roommate.lifestyle.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;

import java.util.HashMap;
import java.util.Map;

import static dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleType.*;

/**
 * 개인 라이프 스타일 조회 Dto
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LifestyleResponseDto {

    private final Map<String, Object> properties = new HashMap<>();

    public static LifestyleResponseDto fromEntity(Lifestyle lifestyle) {
        LifestyleResponseDto responseDto = new LifestyleResponseDto();

        //필수 정보
        responseDto.properties.put(SLEEP_TIME.getType(), lifestyle.getSleepTimeType().getDescription());
        responseDto.properties.put(WAKE_UP_TIME.getType(), lifestyle.getWakeUpTimeType().getDescription());
        responseDto.properties.put(SLEEPING_HABIT.getType(), lifestyle.getSleepingHabitType().getDescription());
        responseDto.properties.put(SLEEPING_SENSITIVITY.getType(), lifestyle.getSleepingSensitivityType().getDescription());
        responseDto.properties.put(SMOKING.getType(), lifestyle.getSmokingType().getDescription());
        responseDto.properties.put(DRINKING_FREQUENCY.getType(), lifestyle.getDrinkingFrequencyType().getDescription());
        responseDto.properties.put(CLEANING_FREQUENCY.getType(), lifestyle.getCleaningFrequencyType().getDescription());
        responseDto.properties.put(HEAT_TOLERANCE.getType(), lifestyle.getHeatToleranceType().getDescription());
        responseDto.properties.put(COLD_TOLERANCE.getType(), lifestyle.getColdToleranceType().getDescription());
        responseDto.properties.put(PERFUME_USAGE.getType(), lifestyle.getPerfumeUsageType().getDescription());
        responseDto.properties.put(EXAM_PREPARATION.getType(), lifestyle.getExamPreparationType().getDescription());

        //선택 정보
        if (lifestyle.getDrunkHabit() != null) {
            responseDto.properties.put("drunkHabit", lifestyle.getDrunkHabit());
        }
        if (lifestyle.getShowerTimeType() != null) {
            responseDto.properties.put(SHOWER_TIME.getType(), lifestyle.getShowerTimeType().getDescription());
        }
        if (lifestyle.getShowerDurationType() != null) {
            responseDto.properties.put(SHOWER_DURATION.getType(), lifestyle.getShowerDurationType().getDescription());
        }
        if (lifestyle.getMbtiType() != null) {
            responseDto.properties.put(MBTI.getType(), lifestyle.getMbtiType().toString());
        }
        if (lifestyle.getVisitHomeFrequencyType() != null) {
            responseDto.properties.put(VISIT_HOME_FREQUENCY.getType(), lifestyle.getVisitHomeFrequencyType().getDescription());
        }
        if (lifestyle.getLateNightSnackType() != null) {
            responseDto.properties.put(LATE_NIGHT_SNACK.getType(), lifestyle.getLateNightSnackType().getDescription());
        }
        if (lifestyle.getSnackInRoomType() != null) {
            responseDto.properties.put(SNACK_IN_ROOM.getType(), lifestyle.getSnackInRoomType().getDescription());
        }
        if (lifestyle.getPhoneSoundType() != null) {
            responseDto.properties.put(PHONE_SOUND.getType(), lifestyle.getPhoneSoundType().getDescription());
        }
        if (lifestyle.getStudyLocationType() != null) {
            responseDto.properties.put(STUDY_LOCATION.getType(), lifestyle.getStudyLocationType().getDescription());
        }
        if (lifestyle.getExerciseType() != null) {
            responseDto.properties.put(EXERCISE.getType(), lifestyle.getExerciseType().getDescription());
        }
        if (lifestyle.getInsectToleranceType() != null) {
            responseDto.properties.put(INSECT_TOLERANCE.getType(), lifestyle.getInsectToleranceType().getDescription());
        }

        return responseDto;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}
