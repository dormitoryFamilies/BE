package dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.request;

import jakarta.validation.constraints.Size;

public record UpdateMyLifestyleRequestDto(

        @Size(max = 12, message = "최대 글자수는 12자 입니다.")
        String drunkHabit,

        String sleepTime,
        String wakeUpTime,
        String sleepingHabit,
        String sleepingSensitivity,
        String smoking,
        String drinkingFrequency,
        String cleaningFrequency,
        String heatTolerance,
        String coldTolerance,
        String perfumeUsage,
        String examPreparation,
        String showerTime,
        String showerDuration,
        String MBTI,
        String visitHomeFrequency,
        String lateNightSnack,
        String snackInRoom,
        String phoneSound,
        String studyLocation,
        String exercise,
        String insectTolerance

) {
}
