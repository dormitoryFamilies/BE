package dormitoryfamily.doomz.domain.roomate.entity;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.type.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLifestyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_lifestyle_id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private CleaningHabitType cleaningHabitType;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Enumerated(EnumType.STRING)
    private SleepTimeType sleepTimeType;

    @Enumerated(EnumType.STRING)
    private SmokingType smokingType;

    @Enumerated(EnumType.STRING)
    private WakeUpTimeType wakeUpTimeType;

    @Builder
    public MyLifestyle(Member member,
                       CleaningHabitType cleaningHabitType,
                       ExerciseType exerciseType,
                       SleepTimeType sleepTimeType,
                       SmokingType smokingType,
                       WakeUpTimeType wakeUpTimeType) {
        this.member = member;
        this.cleaningHabitType = cleaningHabitType;
        this.exerciseType = exerciseType;
        this.sleepTimeType = sleepTimeType;
        this.smokingType = smokingType;
        this.wakeUpTimeType = wakeUpTimeType;
    }
}
