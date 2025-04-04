package dormitoryfamily.doomz.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.entity.type.*;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.*;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final LifestyleRepository lifestyleRepository;
    private final PreferenceOrderRepository preferenceOrderRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final Random random = new Random();
    
    private static final String LIFESTYLE_INDEX = "lifestyle_vectors";
    private static final String PREFERENCE_INDEX = "preference_vectors";

    @Bean
    @Profile("dev")
    @Transactional
    public CommandLineRunner initializeData() {
        return args -> {
            createAndSaveDummyData();
        };
    }

    @Transactional
    public void createAndSaveDummyData() {
        if (memberRepository.count() > 10) {
            System.out.println("데이터베이스에 이미 데이터가 있습니다. 초기화를 건너뜁니다.");
            return;
        }

        List<Member> members = createDummyMembers(50);
        memberRepository.saveAll(members);

        List<Lifestyle> lifestyles = createLifestyles(members);
        lifestyleRepository.saveAll(lifestyles);
        
        // 라이프스타일 엘라스틱서치 인덱싱
        for (int i = 0; i < members.size(); i++) {
            indexLifestyleVector(members.get(i), lifestyles.get(i));
        }

        List<PreferenceOrder> preferenceOrders = createPreferenceOrders(members);
        preferenceOrderRepository.saveAll(preferenceOrders);
        
        // 선호도 엘라스틱서치 인덱싱
        for (int i = 0; i < members.size(); i++) {
            indexPreferenceVector(members.get(i).getId(), preferenceOrders.get(i));
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
    
    private void indexLifestyleVector(Member member, Lifestyle lifestyle) {
        try {
            float[] vector = convertToVector(lifestyle);

            Map<String, Object> document = new HashMap<>();
            document.put("member_id", member.getId());
            document.put("lifestyle_vector", vector);
            document.put("dormitory", member.getDormitoryType().name());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(LIFESTYLE_INDEX)
                    .id(member.getId().toString())
                    .document(document)
            );

            IndexResponse response = elasticsearchClient.index(request);

            if (response.result() != Result.Created && response.result() != Result.Updated) {
                System.err.println("라이프스타일 벡터 저장은 됐지만 예외적인 상태: " + response.result());
            }

        } catch (IOException e) {
            System.err.println("라이프스타일 엘라스틱서치 저장 실패: " + e.getMessage());
            throw new RuntimeException("라이프스타일 엘라스틱서치 저장 중 오류 발생", e);
        }
    }
    
    private float[] convertToVector(Lifestyle lifestyle) {
        float[] vector = new float[11];
        vector[0] = lifestyle.getSleepTimeType().getIndex();
        vector[1] = lifestyle.getWakeUpTimeType().getIndex();
        vector[2] = lifestyle.getSleepingHabitType().getIndex();
        vector[3] = lifestyle.getSleepingSensitivityType().getIndex();
        vector[4] = lifestyle.getSmokingType().getIndex();
        vector[5] = lifestyle.getDrinkingFrequencyType().getIndex();
        vector[6] = lifestyle.getCleaningFrequencyType().getIndex();
        vector[7] = lifestyle.getHeatToleranceType().getIndex();
        vector[8] = lifestyle.getColdToleranceType().getIndex();
        vector[9] = lifestyle.getPerfumeUsageType().getIndex();
        vector[10] = lifestyle.getExamPreparationType().getIndex();
        return vector;
    }
    
    private void indexPreferenceVector(Long memberId, PreferenceOrder order) {
        try {
            float[] weightVector = new float[11];
            float[] preferredValues = new float[11];
            for (int i = 0; i < 11; i++) {
                weightVector[i] = 0.1f;
                preferredValues[i] = 0.0f;
            }

            setWeightAndValue(weightVector, preferredValues, order.getFirstPreferenceOrder(), 1.0f);
            setWeightAndValue(weightVector, preferredValues, order.getSecondPreferenceOrder(), 0.7f);
            setWeightAndValue(weightVector, preferredValues, order.getThirdPreferenceOrder(), 0.5f);
            setWeightAndValue(weightVector, preferredValues, order.getFourthPreferenceOrder(), 0.2f);

            Map<String, Object> document = new HashMap<>();
            document.put("member_id", memberId);
            document.put("preference_weight", weightVector);
            document.put("preferred_values", preferredValues);
            document.put("dormitory", order.getMember().getDormitoryType().name());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(PREFERENCE_INDEX)
                    .id(memberId.toString())
                    .document(document));

            IndexResponse response = elasticsearchClient.index(request);
            if (response.result() != Result.Created && response.result() != Result.Updated) {
                System.err.println("선호도 벡터 저장 예외 상태: " + response.result());
            }
        } catch (IOException e) {
            System.err.println("선호도 엘라스틱서치 저장 실패: " + e.getMessage());
            throw new RuntimeException("선호도 엘라스틱서치 저장 중 오류 발생", e);
        }
    }
    
    private void setWeightAndValue(float[] vector, float[] values, Enum<?> preference, float weight) {
        LifestyleType type = LifestyleType.fromTypeName(preference.getClass().getSimpleName());
        int index = type.getVectorIndex();
        if (index >= 0 && index < vector.length) {
            vector[index] = weight;
            values[index] = ((LifestyleAttribute) preference).getIndex();
        }
    }
} 