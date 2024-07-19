package dormitoryfamily.doomz.domain.roomate.dto.lifestyle.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;

import java.util.HashMap;
import java.util.Map;

import static dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType.*;

/**
 * 개인 라이프 스타일 조회 Dto
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LifestyleResponseDto {

    private final Map<String, Object> properties = new HashMap<>();

    public static LifestyleResponseDto fromEntity(MyLifestyle myLifestyle) {
        LifestyleResponseDto responseDto = new LifestyleResponseDto();

        //필수 정보
        responseDto.properties.put(SLEEP_TIME.getType(), myLifestyle.getSleepTimeType().getDescription());
        responseDto.properties.put(WAKE_UP_TIME.getType(), myLifestyle.getWakeUpTimeType().getDescription());
        responseDto.properties.put(SLEEPING_HABIT.getType(), myLifestyle.getSleepingHabitType().getDescription());
        responseDto.properties.put(SLEEPING_SENSITIVITY.getType(), myLifestyle.getSleepingSensitivityType().getDescription());
        responseDto.properties.put(SMOKING.getType(), myLifestyle.getSmokingType().getDescription());
        responseDto.properties.put(DRINKING_FREQUENCY.getType(), myLifestyle.getDrinkingFrequencyType().getDescription());
        responseDto.properties.put(CLEANING_FREQUENCY.getType(), myLifestyle.getCleaningFrequencyType().getDescription());
        responseDto.properties.put(HEAT_TOLERANCE.getType(), myLifestyle.getHeatToleranceType().getDescription());
        responseDto.properties.put(COLD_TOLERANCE.getType(), myLifestyle.getColdToleranceType().getDescription());
        responseDto.properties.put(PERFUME_USAGE.getType(), myLifestyle.getPerfumeUsageType().getDescription());
        responseDto.properties.put(EXAM_PREPARATION.getType(), myLifestyle.getExamPreparationType().getDescription());

        //선택 정보
        if (myLifestyle.getDrunkHabit() != null) {
            responseDto.properties.put("drunkHabit", myLifestyle.getDrunkHabit());
        }
        if (myLifestyle.getShowerTimeType() != null) {
            responseDto.properties.put(SHOWER_TIME.getType(), myLifestyle.getShowerTimeType().getDescription());
        }
        if (myLifestyle.getShowerDurationType() != null) {
            responseDto.properties.put(SHOWER_DURATION.getType(), myLifestyle.getShowerDurationType().getDescription());
        }
        if (myLifestyle.getMbtiType() != null) {
            responseDto.properties.put(MBTI.getType(), myLifestyle.getMbtiType().toString());
        }
        if (myLifestyle.getVisitHomeFrequencyType() != null) {
            responseDto.properties.put(VISIT_HOME_FREQUENCY.getType(), myLifestyle.getVisitHomeFrequencyType().getDescription());
        }
        if (myLifestyle.getLateNightSnackType() != null) {
            responseDto.properties.put(LATE_NIGHT_SNACK.getType(), myLifestyle.getLateNightSnackType().getDescription());
        }
        if (myLifestyle.getSnackInRoomType() != null) {
            responseDto.properties.put(SNACK_IN_ROOM.getType(), myLifestyle.getSnackInRoomType().getDescription());
        }
        if (myLifestyle.getPhoneSoundType() != null) {
            responseDto.properties.put(PHONE_SOUND.getType(), myLifestyle.getPhoneSoundType().getDescription());
        }
        if (myLifestyle.getStudyLocationType() != null) {
            responseDto.properties.put(STUDY_LOCATION.getType(), myLifestyle.getStudyLocationType().getDescription());
        }
        if (myLifestyle.getExerciseType() != null) {
            responseDto.properties.put(EXERCISE.getType(), myLifestyle.getExerciseType().getDescription());
        }
        if (myLifestyle.getInsectToleranceType() != null) {
            responseDto.properties.put(INSECT_TOLERANCE.getType(), myLifestyle.getInsectToleranceType().getDescription());
        }

        return responseDto;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}
