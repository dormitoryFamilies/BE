package dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.request;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.type.*;

public record MyLifestyleRequestDto(
        String sleepTime,
        String wakeUpTime,
        String sleepingHabit,
        String sleepingSensitivity,
        String smoking,
        String drinkingFrequency,
        String drunkHabit,
        String showerTime,
        String showerDuration,
        String cleaningFrequency,
        String heatTolerance,
        String coldTolerance,
        String MBTI,
        String visitHomeFrequency,
        String lateNightSnack,
        String snackInRoom,
        String phoneSound,
        String perfumeUsage,
        String studyLocation,
        String examPreparation,
        String exercise,
        String insectTolerance

) {
    public static MyLifestyle toEntity(Member member, MyLifestyleRequestDto requestDto) {
        return MyLifestyle.builder()
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
