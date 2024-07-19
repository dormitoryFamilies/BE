package dormitoryfamily.doomz.domain.roomate.entity;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.type.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private SleepTimeType sleepTimeType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WakeUpTimeType wakeUpTimeType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SleepingHabitType sleepingHabitType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SleepingSensitivityType sleepingSensitivityType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SmokingType smokingType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DrinkingFrequencyType drinkingFrequencyType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ShowerTimeType showerTimeType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ShowerDurationType showerDurationType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CleaningFrequencyType cleaningFrequencyType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HeatToleranceType heatToleranceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ColdToleranceType coldToleranceType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private MBTIType mbtiType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private VisitHomeFrequencyType visitHomeFrequencyType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private LateNightSnackType lateNightSnackType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private SnackInRoomType snackInRoomType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private PhoneSoundType phoneSoundType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PerfumeUsageType perfumeUsageType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private StudyLocationType studyLocationType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExamPreparationType examPreparationType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Nullable
    @Enumerated(EnumType.STRING)
    private InsectToleranceType insectToleranceType;

    @Nullable
    private String drunkHabit;

    @Builder
    public MyLifestyle(Member member,
                       SleepTimeType sleepTimeType,
                       WakeUpTimeType wakeUpTimeType,
                       SleepingHabitType sleepingHabitType,
                       SleepingSensitivityType sleepingSensitivityType,
                       SmokingType smokingType,
                       DrinkingFrequencyType drinkingFrequencyType,
                       ShowerTimeType showerTimeType,
                       ShowerDurationType showerDurationType,
                       CleaningFrequencyType cleaningFrequencyType,
                       HeatToleranceType heatToleranceType,
                       ColdToleranceType coldToleranceType,
                       MBTIType mbtiType,
                       VisitHomeFrequencyType visitHomeFrequencyType,
                       LateNightSnackType lateNightSnackType,
                       SnackInRoomType snackInRoomType,
                       PhoneSoundType phoneSoundType,
                       PerfumeUsageType perfumeUsageType,
                       StudyLocationType studyLocationType,
                       ExamPreparationType examPreparationType,
                       ExerciseType exerciseType,
                       InsectToleranceType insectToleranceType,
                       String drunkHabit
    ) {
        this.member = member;
        this.sleepTimeType = sleepTimeType;
        this.wakeUpTimeType = wakeUpTimeType;
        this.sleepingHabitType = sleepingHabitType;
        this.sleepingSensitivityType = sleepingSensitivityType;
        this.smokingType = smokingType;
        this.drinkingFrequencyType = drinkingFrequencyType;
        this.showerTimeType = showerTimeType;
        this.showerDurationType = showerDurationType;
        this.cleaningFrequencyType = cleaningFrequencyType;
        this.heatToleranceType = heatToleranceType;
        this.coldToleranceType = coldToleranceType;
        this.mbtiType = mbtiType;
        this.visitHomeFrequencyType = visitHomeFrequencyType;
        this.lateNightSnackType = lateNightSnackType;
        this.snackInRoomType = snackInRoomType;
        this.phoneSoundType = phoneSoundType;
        this.perfumeUsageType = perfumeUsageType;
        this.studyLocationType = studyLocationType;
        this.examPreparationType = examPreparationType;
        this.exerciseType = exerciseType;
        this.insectToleranceType = insectToleranceType;
        this.drunkHabit = drunkHabit;
    }
}
