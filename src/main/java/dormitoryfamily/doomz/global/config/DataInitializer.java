package dormitoryfamily.doomz.global.config;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.entity.type.*;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.*;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final LifestyleRepository lifestyleRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final ElasticScriptQueryExecutor elasticScriptQueryExecutor;
    private final Random random = new Random();

    @Transactional
    public void createAndSaveDummyData() {

        List<Member> members = createDummyMembers(50);
        memberRepository.saveAll(members);

        List<Lifestyle> lifestyles = createLifestyles(members);
        lifestyleRepository.saveAll(lifestyles);
        lifestyleRepository.flush();

        // 라이프스타일 엘라스틱서치 인덱싱
        for (int i = 0; i < members.size(); i++) {
            elasticScriptQueryExecutor.indexLifestyleVector(members.get(i), lifestyles.get(i));
        }

        List<PreferenceOrder> preferenceOrders = createPreferenceOrders(members);
        preferenceOrderRepository.saveAll(preferenceOrders);
        preferenceOrderRepository.flush();

        // 선호도 엘라스틱서치 인덱싱
        for (int i = 0; i < members.size(); i++) {
            elasticScriptQueryExecutor.indexPreferenceVector(members.get(i).getId(), preferenceOrders.get(i), members.get(i));
        }

        System.out.println("더미 데이터 초기화 완료!");
    }

    private List<Member> createDummyMembers(int count) {
        List<Member> members = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            GenderType gender = random.nextBoolean() ? GenderType.MALE : GenderType.FEMALE;
            CollegeType college = getRandomEnum(CollegeType.values());
            DepartmentType department = getRandomDepartment(college);
            MemberDormitoryType dormitory = getRandomEnum(MemberDormitoryType.values());

            Member member = Member.builder()
                    .name("테스트" + i)
                    .nickname("닉네임" + i)
                    .email("test" + i + "@example.com")
                    .studentNumber("202401" + String.format("%03d", i))
                    .collegeType(college)
                    .departmentType(department)
                    .dormitoryType(dormitory)
                    .birthDate(LocalDate.now().minusYears(20 + random.nextInt(5)))
                    .genderType(gender)
                    .profileUrl("https://example.com/profile" + i + ".jpg")
                    .studentCardImageUrl("https://example.com/studentCard" + i + ".jpg")
                    .followingCount(random.nextInt(50))
                    .followerCount(random.nextInt(50))
                    .isRoommateMatched(false)
                    .authority(RoleType.ROLE_VERIFIED_STUDENT)
                    .build();

            members.add(member);
        }

        return members;
    }

    private List<Lifestyle> createLifestyles(List<Member> members) {
        List<Lifestyle> lifestyles = new ArrayList<>();

        for (Member member : members) {
            Lifestyle lifestyle = Lifestyle.builder()
                    .member(member)
                    .sleepTimeType(getRandomEnum(SleepTimeType.values()))
                    .wakeUpTimeType(getRandomEnum(WakeUpTimeType.values()))
                    .sleepingHabitType(getRandomEnum(SleepingHabitType.values()))
                    .sleepingSensitivityType(getRandomEnum(SleepingSensitivityType.values()))
                    .smokingType(getRandomEnum(SmokingType.values()))
                    .drinkingFrequencyType(getRandomEnum(DrinkingFrequencyType.values()))
                    .showerTimeType(getRandomEnum(ShowerTimeType.values()))
                    .showerDurationType(getRandomEnum(ShowerDurationType.values()))
                    .cleaningFrequencyType(getRandomEnum(CleaningFrequencyType.values()))
                    .heatToleranceType(getRandomEnum(HeatToleranceType.values()))
                    .coldToleranceType(getRandomEnum(ColdToleranceType.values()))
                    .mbtiType(getRandomEnum(MBTIType.values()))
                    .visitHomeFrequencyType(getRandomEnum(VisitHomeFrequencyType.values()))
                    .lateNightSnackType(getRandomEnum(LateNightSnackType.values()))
                    .snackInRoomType(getRandomEnum(SnackInRoomType.values()))
                    .phoneSoundType(getRandomEnum(PhoneSoundType.values()))
                    .perfumeUsageType(getRandomEnum(PerfumeUsageType.values()))
                    .studyLocationType(getRandomEnum(StudyLocationType.values()))
                    .examPreparationType(getRandomEnum(ExamPreparationType.values()))
                    .exerciseType(getRandomEnum(ExerciseType.values()))
                    .insectToleranceType(getRandomEnum(InsectToleranceType.values()))
                    .drunkHabit(random.nextBoolean() ? "술 마시면 시끄러워요" : null)
                    .build();

            lifestyles.add(lifestyle);
        }

        return lifestyles;
    }

    private List<PreferenceOrder> createPreferenceOrders(List<Member> members) {
        List<PreferenceOrder> preferenceOrders = new ArrayList<>();

        for (Member member : members) {
            // 중요한 라이프스타일 속성 중에서 4개를 랜덤으로 선택
            List<LifestyleType> essentialTypes = getEssentialLifestyleTypes();
            List<LifestyleType> selectedTypes = selectRandomTypes(essentialTypes, 4);

            Enum<?> firstPreference = getRandomEnumForType(selectedTypes.get(0));
            Enum<?> secondPreference = getRandomEnumForType(selectedTypes.get(1));
            Enum<?> thirdPreference = getRandomEnumForType(selectedTypes.get(2));
            Enum<?> fourthPreference = getRandomEnumForType(selectedTypes.get(3));

            PreferenceOrder preferenceOrder = PreferenceOrder.builder()
                    .member(member)
                    .firstPreferenceOrder(firstPreference)
                    .secondPreferenceOrder(secondPreference)
                    .thirdPreferenceOrder(thirdPreference)
                    .fourthPreferenceOrder(fourthPreference)
                    .build();

            preferenceOrders.add(preferenceOrder);
        }

        return preferenceOrders;
    }

    private List<LifestyleType> getEssentialLifestyleTypes() {
        List<LifestyleType> essential = new ArrayList<>();
        for (LifestyleType type : LifestyleType.values()) {
            if (type.isEssential()) {
                essential.add(type);
            }
        }
        return essential;
    }

    private List<LifestyleType> selectRandomTypes(List<LifestyleType> types, int count) {
        List<LifestyleType> copyList = new ArrayList<>(types);
        List<LifestyleType> result = new ArrayList<>();

        for (int i = 0; i < count && !copyList.isEmpty(); i++) {
            int index = random.nextInt(copyList.size());
            result.add(copyList.remove(index));
        }

        return result;
    }

    private <T extends Enum<?>> T getRandomEnum(T[] values) {
        return values[random.nextInt(values.length)];
    }

    private DepartmentType getRandomDepartment(CollegeType collegeType) {
        List<DepartmentType> departmentsForCollege = new ArrayList<>();
        for (DepartmentType dept : DepartmentType.values()) {
            if (dept.getCollege().equals(collegeType)) {
                departmentsForCollege.add(dept);
            }
        }

        if (departmentsForCollege.isEmpty()) {
            // 대학이 없는 경우 자율전공학부 반환
            return DepartmentType.SELF_DEVELOPMENT_MAJOR;
        }

        return departmentsForCollege.get(random.nextInt(departmentsForCollege.size()));
    }

    private Enum<?> getRandomEnumForType(LifestyleType type) {
        return switch (type) {
            case SLEEP_TIME -> getRandomEnum(SleepTimeType.values());
            case WAKE_UP_TIME -> getRandomEnum(WakeUpTimeType.values());
            case SLEEPING_HABIT -> getRandomEnum(SleepingHabitType.values());
            case SLEEPING_SENSITIVITY -> getRandomEnum(SleepingSensitivityType.values());
            case SMOKING -> getRandomEnum(SmokingType.values());
            case DRINKING_FREQUENCY -> getRandomEnum(DrinkingFrequencyType.values());
            case CLEANING_FREQUENCY -> getRandomEnum(CleaningFrequencyType.values());
            case HEAT_TOLERANCE -> getRandomEnum(HeatToleranceType.values());
            case COLD_TOLERANCE -> getRandomEnum(ColdToleranceType.values());
            case PERFUME_USAGE -> getRandomEnum(PerfumeUsageType.values());
            case EXAM_PREPARATION -> getRandomEnum(ExamPreparationType.values());
            default -> null;
        };
    }


}