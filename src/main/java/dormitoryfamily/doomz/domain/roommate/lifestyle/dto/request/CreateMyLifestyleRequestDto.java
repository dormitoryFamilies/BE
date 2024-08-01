package dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMyLifestyleRequestDto(

        @NotBlank(message = "필수 값 입니다.")
        String sleepTime,

        @NotBlank(message = "필수 값 입니다.")
        String wakeUpTime,

        @NotBlank(message = "필수 값 입니다.")
        String sleepingHabit,

        @NotBlank(message = "필수 값 입니다.")
        String sleepingSensitivity,

        @NotBlank(message = "필수 값 입니다.")
        String smoking,

        @NotBlank(message = "필수 값 입니다.")
        String drinkingFrequency,

        @NotBlank(message = "필수 값 입니다.")
        String cleaningFrequency,

        @NotBlank(message = "필수 값 입니다.")
        String heatTolerance,

        @NotBlank(message = "필수 값 입니다.")
        String coldTolerance,

        @NotBlank(message = "필수 값 입니다.")
        String perfumeUsage,

        @NotBlank(message = "필수 값 입니다.")
        String examPreparation,

        @Size(max = 12, message = "최대 글자수는 12자 입니다.")
        String drunkHabit,

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
    public static Lifestyle toEntity(Member member, CreateMyLifestyleRequestDto requestDto) {
        return Lifestyle.builder()
                .member(member)
                .sleepTimeType(SleepTimeType.fromDescription(requestDto.sleepTime()))
                .wakeUpTimeType(WakeUpTimeType.fromDescription(requestDto.wakeUpTime()))
                .sleepingHabitType(SleepingHabitType.fromDescription(requestDto.sleepingHabit()))
                .sleepingSensitivityType(SleepingSensitivityType.fromDescription(requestDto.sleepingSensitivity()))
                .smokingType(SmokingType.fromDescription(requestDto.smoking()))
                .drinkingFrequencyType(DrinkingFrequencyType.fromDescription(requestDto.drinkingFrequency()))
                .showerTimeType(ShowerTimeType.fromDescription(requestDto.showerTime()))
                .showerDurationType(ShowerDurationType.fromDescription(requestDto.showerDuration()))
                .cleaningFrequencyType(CleaningFrequencyType.fromDescription(requestDto.cleaningFrequency()))
                .heatToleranceType(HeatToleranceType.fromDescription(requestDto.heatTolerance()))
                .coldToleranceType(ColdToleranceType.fromDescription(requestDto.coldTolerance()))
                .mbtiType(MBTIType.fromDescription(requestDto.MBTI()))
                .visitHomeFrequencyType(VisitHomeFrequencyType.fromDescription(requestDto.visitHomeFrequency()))
                .lateNightSnackType(LateNightSnackType.fromDescription(requestDto.lateNightSnack()))
                .snackInRoomType(SnackInRoomType.fromDescription(requestDto.snackInRoom()))
                .phoneSoundType(PhoneSoundType.fromDescription(requestDto.phoneSound()))
                .perfumeUsageType(PerfumeUsageType.fromDescription(requestDto.perfumeUsage()))
                .studyLocationType(StudyLocationType.fromDescription(requestDto.studyLocation()))
                .examPreparationType(ExamPreparationType.fromDescription(requestDto.examPreparation()))
                .exerciseType(ExerciseType.fromDescription(requestDto.exercise()))
                .insectToleranceType(InsectToleranceType.fromDescription(requestDto.insectTolerance()))
                .drunkHabit(requestDto.drunkHabit())
                .build();
    }
}
