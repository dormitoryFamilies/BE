package dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.request;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.type.*;

public record MyLifestyleRequestDto(
        String cleaningHabitType,
        String exerciseType,
        String sleepTimeType,
        String smokingType,
        String wakeUpTimeType
) {
    public static MyLifestyle toEntity(Member member, MyLifestyleRequestDto requestDto) {
        return MyLifestyle.builder()
                .member(member)
                .cleaningHabitType(CleaningHabitType.fromDescription(requestDto.cleaningHabitType))
                .exerciseType(ExerciseType.fromDescription(requestDto.exerciseType))
                .sleepTimeType(SleepTimeType.fromDescription(requestDto.sleepTimeType))
                .smokingType(SmokingType.fromDescription(requestDto.smokingType))
                .wakeUpTimeType(WakeUpTimeType.fromDescription(requestDto.wakeUpTimeType))
                .build();
    }
}
