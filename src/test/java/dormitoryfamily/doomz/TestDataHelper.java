package dormitoryfamily.doomz;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.board.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.board.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.entity.type.*;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.*;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Candidate;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class TestDataHelper {

    /**
     * 멤머 더미 데이터 생성
     */
    public static Map<String, Member> createDummyMembers() {
        return Map.ofEntries(
                entry("홍길동", createMember(1L, "홍길동(진리관)", "공대여신", "hong1@example.com", "1234567890", CollegeType.ENGINEERING, DepartmentType.KOREAN_LITERATURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1990, 2, 11), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile1.jpg", "https://s3.amazonaws.com/yourbucket/student1.jpg", 12, 4, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("김영희", createMember(2L, "김영희(정의관)", "철학소녀", "kim2@example.com", "0987654321", CollegeType.HUMANITIES, DepartmentType.PHILOSOPHY, MemberDormitoryType.JINRIGWAN, LocalDate.of(1992, 5, 18), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile2.jpg", "https://s3.amazonaws.com/yourbucket/student2.jpg", 34, 7, true, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("이철수", createMember(3L, "이철수(개척관)", "자연남자", "lee3@example.com", "1122334455", CollegeType.NATURAL_SCIENCES, DepartmentType.HISTORY, MemberDormitoryType.JINRIGWAN, LocalDate.of(1995, 3, 12), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile3.jpg", "https://s3.amazonaws.com/yourbucket/student3.jpg", 23, 12, true, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("박민수", createMember(4L, "박민수(계영원)", "사회과학자", "park4@example.com", "2233445566", CollegeType.SOCIAL_SCIENCES, DepartmentType.GERMAN_LANGUAGE_CULTURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1988, 8, 30), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile4.jpg", "https://s3.amazonaws.com/yourbucket/student4.jpg", 56, 22, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("정수진", createMember(5L, "정수진(지선관)", "경영왕자", "jung5@example.com", "3344556677", CollegeType.BUSINESS, DepartmentType.FRENCH_LANGUAGE_CULTURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1991, 11, 22), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile5.jpg", "https://s3.amazonaws.com/yourbucket/student5.jpg", 67, 13, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("오세진", createMember(6L, "오세진(명덕관)", "전자천재", "oh6@example.com", "4455667788", CollegeType.ELECTRONICS_INFORMATION, DepartmentType.RUSSIAN_LANGUAGE_CULTURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1994, 7, 15), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile6.jpg", "https://s3.amazonaws.com/yourbucket/student6.jpg", 45, 19, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("이정환", createMember(7L, "이정환(신민관)", "농업천재", "lee7@example.com", "5566778899", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT, DepartmentType.ARCHAEOLOGY_ART_HISTORY, MemberDormitoryType.JINRIGWAN, LocalDate.of(1989, 1, 5), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile7.jpg", "https://s3.amazonaws.com/yourbucket/student7.jpg", 28, 9, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("김민지", createMember(8L, "김민지(인의관)", "약학소녀", "kim8@example.com", "6677889900", CollegeType.PHARMACY, DepartmentType.ENGLISH_LITERATURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1996, 12, 1), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile8.jpg", "https://s3.amazonaws.com/yourbucket/student8.jpg", 90, 30, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("박지훈", createMember(9L, "박지훈(예지관)", "의학천재", "park9@example.com", "7788990011", CollegeType.MEDICINE, DepartmentType.CHINESE_LITERATURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1993, 4, 17), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile9.jpg", "https://s3.amazonaws.com/yourbucket/student9.jpg", 12, 5, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("강수진", createMember(10L, "강수진(등용관)", "생명공학자", "kang10@example.com", "8899001122", CollegeType.BIOHEALTH_SHARED, DepartmentType.PHILOSOPHY, MemberDormitoryType.JINRIGWAN, LocalDate.of(1990, 9, 23), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile10.jpg", "https://s3.amazonaws.com/yourbucket/student10.jpg", 40, 1, true, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("이민호", createMember(11L, "이민호(진리관)", "전공왕자", "lee11@example.com", "9900112233", CollegeType.SELF_DEVELOPMENT_MAJOR, DepartmentType.HISTORY, MemberDormitoryType.JINRIGWAN, LocalDate.of(1992, 2, 14), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile11.jpg", "https://s3.amazonaws.com/yourbucket/student11.jpg", 50, 25, true, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("김하늘", createMember(12L, "김하늘(정의관)", "융합여신", "kim12@example.com", "1011223344", CollegeType.CONVERGENCE_DEPARTMENTS, DepartmentType.GERMAN_LANGUAGE_CULTURE, MemberDormitoryType.JEONGUIGWAN, LocalDate.of(1995, 5, 28), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile12.jpg", "https://s3.amazonaws.com/yourbucket/student12.jpg", 75, 35, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("박철수", createMember(13L, "박철수(개척관)", "공대천재", "park13@example.com", "1122334455", CollegeType.ENGINEERING, DepartmentType.ARCHAEOLOGY_ART_HISTORY, MemberDormitoryType.GAECHEOKGWAN, LocalDate.of(1988, 11, 9), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile13.jpg", "https://s3.amazonaws.com/yourbucket/student13.jpg", 65, 29, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("홍서연", createMember(14L, "홍서연(계영원)", "인문대여신", "hong14@example.com", "2233445566", CollegeType.HUMANITIES, DepartmentType.KOREAN_LITERATURE, MemberDormitoryType.GYEYOUNGWON, LocalDate.of(1991, 3, 21), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile14.jpg", "https://s3.amazonaws.com/yourbucket/student14.jpg", 80, 40, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("최민수", createMember(15L, "최민수(지선관)", "바이오천재", "choi15@example.com", "3344556677", CollegeType.BIOHEALTH, DepartmentType.FRENCH_LANGUAGE_CULTURE, MemberDormitoryType.JISEONGWAN, LocalDate.of(1994, 7, 11), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile15.jpg", "https://s3.amazonaws.com/yourbucket/student15.jpg", 37, 20, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("유리", createMember(16L, "유리(명덕관)", "화학천재", "yoo16@example.com", "4455667788", CollegeType.NATURAL_SCIENCES, DepartmentType.KOREAN_LITERATURE, MemberDormitoryType.MYEONGDEOKGWAN, LocalDate.of(1994, 7, 16), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile16.jpg", "https://s3.amazonaws.com/yourbucket/student16.jpg", 14, 5, true, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("민혁", createMember(17L, "민혁(신민관)", "전기천재", "min17@example.com", "5566778899", CollegeType.ENGINEERING, DepartmentType.PHILOSOPHY, MemberDormitoryType.SINMINGWAN, LocalDate.of(1993, 8, 17), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile17.jpg", "https://s3.amazonaws.com/yourbucket/student17.jpg", 44, 15, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("민수", createMember(18L, "민수(인의관)", "컴퓨터천재", "min18@example.com", "6677889900", CollegeType.ELECTRONICS_INFORMATION, DepartmentType.GERMAN_LANGUAGE_CULTURE, MemberDormitoryType.INUIGWAN, LocalDate.of(1990, 4, 25), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile18.jpg", "https://s3.amazonaws.com/yourbucket/student18.jpg", 33, 11, false, RoleType.ROLE_VERIFIED_STUDENT)),
                entry("소희", createMember(19L, "소희(예지관)", "생물학천재", "soh19@example.com", "7788990011", CollegeType.BIOHEALTH, DepartmentType.RUSSIAN_LANGUAGE_CULTURE, MemberDormitoryType.YEJIGWAN, LocalDate.of(1991, 5, 10), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile19.jpg", "https://s3.amazonaws.com/yourbucket/student19.jpg", 20, 8, false, RoleType.ROLE_REJECTED_MEMBER)),
                entry("진수", createMember(20L, "진수(등용관)", "물리학천재", "jin20@example.com", "8899001122", CollegeType.NATURAL_SCIENCES, DepartmentType.CHINESE_LITERATURE, MemberDormitoryType.DEUNGYONGGWAN, LocalDate.of(1993, 1, 21), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile20.jpg", "https://s3.amazonaws.com/yourbucket/student20.jpg", 17, 6, false, RoleType.ROLE_REJECTED_MEMBER)),
                entry("현주", createMember(21L, "현주(진리관)", "화학여신", "hyun21@example.com", "9900112233", CollegeType.NATURAL_SCIENCES, DepartmentType.KOREAN_LITERATURE, MemberDormitoryType.JINRIGWAN, LocalDate.of(1995, 11, 30), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile21.jpg", "https://s3.amazonaws.com/yourbucket/student21.jpg", 55, 22, false, RoleType.ROLE_MEMBER)),
                entry("진혁", createMember(22L, "진혁(정의관)", "수학천재", "jin22@example.com", "1011223344", CollegeType.NATURAL_SCIENCES, DepartmentType.PHILOSOPHY, MemberDormitoryType.JEONGUIGWAN, LocalDate.of(1991, 7, 15), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile22.jpg", "https://s3.amazonaws.com/yourbucket/student22.jpg", 25, 10, true, RoleType.ROLE_MEMBER)),
                entry("가영", createMember(23L, "가영(개척관)", "영문학천재", "ka23@example.com", "1122334455", CollegeType.HUMANITIES, DepartmentType.ENGLISH_LITERATURE, MemberDormitoryType.GAECHEOKGWAN, LocalDate.of(1988, 9, 9), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile23.jpg", "https://s3.amazonaws.com/yourbucket/student23.jpg", 42, 18, false, RoleType.ROLE_MEMBER)),
                entry("민재", createMember(24L, "민재(계영원)", "독문학천재", "min24@example.com", "2233445566", CollegeType.HUMANITIES, DepartmentType.GERMAN_LANGUAGE_CULTURE, MemberDormitoryType.GYEYOUNGWON, LocalDate.of(1994, 3, 3), GenderType.MALE, "https://s3.amazonaws.com/yourbucket/profile24.jpg", "https://s3.amazonaws.com/yourbucket/student24.jpg", 63, 27, false, RoleType.ROLE_MEMBER)),
                entry("서진", createMember(25L, "서진(지선관)", "불문학천재", "seo25@example.com", "3344556677", CollegeType.HUMANITIES, DepartmentType.FRENCH_LANGUAGE_CULTURE, MemberDormitoryType.JISEONGWAN, LocalDate.of(1996, 12, 23), GenderType.FEMALE, "https://s3.amazonaws.com/yourbucket/profile25.jpg", "https://s3.amazonaws.com/yourbucket/student25.jpg", 71, 29, false, RoleType.ROLE_MEMBER))
        );
    }

    /**
     * 게시글 더미 데이터 생성
     */
    public static List<Article> createDummyArticles(Map<String, Member> members) {
        return Arrays.asList(
                // 본관
                createArticle(1L, members.get("홍길동"), "겨울바다 보고 싶다.", "내용무", "S3/images/abc", StatusType.PROGRESS, ArticleDormitoryType.MAIN_BUILDING, BoardType.HELP, "#방학", 0, 0, 0),
                createArticle(2L, members.get("이순신"), "군대가기 싫다ㅠㅠㅠ", "군대는 왜 가야 하나요?", null, StatusType.DONE, ArticleDormitoryType.MALE_DORMITORY, BoardType.CURIOUS, null, 0, 0, 0),
                createArticle(3L, members.get("임꺽정"), "야~~~호", "메~~~~~~~~~롱", null, StatusType.DONE, ArticleDormitoryType.MALE_DORMITORY, BoardType.CURIOUS, "#장난치기", 0, 0, 0),

                // 양진재
                createArticle(4L, members.get("홍길동"), "기숙사 식구들 화이팅!", "생각보다 오래 걸리는 프로젝트네요! 그래도 화이팅!!", "S3/images/abc", StatusType.PROGRESS, ArticleDormitoryType.FEMALE_DORMITORY, BoardType.HELP, "#바퀴벌래#무서워", 0, 0, 0),
                createArticle(5L, members.get("임꺽정"), "학식 같이 먹을 사람 구해요!", "혼자 먹으니깐 너무 심심해요 ㅠㅠ", "S3/imagesdef", StatusType.DONE, ArticleDormitoryType.FEMALE_DORMITORY, BoardType.CURIOUS, "#학식#저녁#친구", 0, 0, 0),

                // 양성재
                createArticle(6L, members.get("이순신"), "바다 오염이 심각하네요..", "우리 주말에 쓰레기 주우러 갑시다!", null, StatusType.PROGRESS, ArticleDormitoryType.MALE_DORMITORY, BoardType.HELP, "#바다#봉사#주말", 0, 0, 0)
        );
    }

    /**
     * 게시글 이미지 더미 데이터 생성
     */
    public static List<ArticleImage> createDummyArticleImages(List<Article> articles) {
        return List.of(
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images1"),
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images2"),
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images3")
        );
    }

    /**
     * 라이프 스타일 더미 데이터 생성
     */
    public static List<Lifestyle> createDummyLifestyles(Map<String, Member> members) {
        return Arrays.asList(
                createLifestyle(1L, members.get("홍길동"), SleepTimeType._2200, WakeUpTimeType._0700, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.FREQUENT, null, ShowerDurationType._05, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.HIGH, null, null, LateNightSnackType.SOMETIMES, SnackInRoomType.ALLOWED, null, PerfumeUsageType.SOMETIMES, null, ExamPreparationType.PREPARING, ExerciseType.GYM, null, "취침 후에 말 많이 함"),
                createLifestyle(2L, members.get("김영희"), SleepTimeType._2300, WakeUpTimeType._0600, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.REGULAR, ShowerTimeType.MORNING, ShowerDurationType._20, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.INFJ, null, LateNightSnackType.OFTEN, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.OFTEN, StudyLocationType.OFF_DORMITORY, ExamPreparationType.PREPARING, null, InsectToleranceType.SMALL_ONLY, "소란 피움"),
                createLifestyle(3L, members.get("이철수"), SleepTimeType._2400, WakeUpTimeType._0800, SleepingHabitType.TEETH_GRINDING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.OCCASIONAL, null, null, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, MBTIType.ISFP, VisitHomeFrequencyType.RARELY, LateNightSnackType.NEVER, SnackInRoomType.ALLOWED, null, PerfumeUsageType.OFTEN, null, ExamPreparationType.NONE, ExerciseType.DORMITORY, InsectToleranceType.CANNOT, "말싸움"),
                createLifestyle(4L, members.get("박민수"), SleepTimeType.BEFORE_2100, WakeUpTimeType.BEFORE_0600, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.NONE, null, ShowerDurationType._10, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.HIGH, MBTIType.ESTJ, null, LateNightSnackType.SOMETIMES, SnackInRoomType.ALLOWED, PhoneSoundType.VARIABLE, PerfumeUsageType.OFTEN, null, ExamPreparationType.PREPARING, ExerciseType.NONE, InsectToleranceType.EXPERT, "술 먹고 춤 춤"),
                createLifestyle(5L, members.get("정수진"), SleepTimeType._0100, WakeUpTimeType._0900, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.NON_SMOKER, DrinkingFrequencyType.FREQUENT, null, ShowerDurationType._15, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.MEDIUM, MBTIType.ISTP, null, null, SnackInRoomType.ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.SOMETIMES, null, ExamPreparationType.NONE, ExerciseType.GYM, InsectToleranceType.SMALL_ONLY, "소리지름"),
                createLifestyle(6L, members.get("오세진"), SleepTimeType.BEFORE_2100, WakeUpTimeType._0700, SleepingHabitType.NONE, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.REGULAR, ShowerTimeType.MORNING, ShowerDurationType._20, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ENFJ, VisitHomeFrequencyType.EVERY_WEEK, LateNightSnackType.OFTEN, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.EARPHONES, PerfumeUsageType.OFTEN, StudyLocationType.VARIABLE, ExamPreparationType.PREPARING, null, InsectToleranceType.SMALL_ONLY, "혼잣말 많이 함"),
                createLifestyle(7L, members.get("이정환"), SleepTimeType._0200, WakeUpTimeType._0800, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.FREQUENT, ShowerTimeType.EVENING, null, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.LOW, ColdToleranceType.HIGH, MBTIType.INTJ, VisitHomeFrequencyType.RARELY, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, PhoneSoundType.VARIABLE, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.NONE, ExerciseType.GYM, InsectToleranceType.EXPERT, "웃음소리 큼"),
                createLifestyle(8L, members.get("김민지"), SleepTimeType._0300, WakeUpTimeType._0600, SleepingHabitType.SNORING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.NONE, ShowerTimeType.MORNING, ShowerDurationType._10, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.MEDIUM, ColdToleranceType.MEDIUM, MBTIType.ENTJ, VisitHomeFrequencyType.EVERY_2_3_MONTHS, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.SOMETIMES, StudyLocationType.OFF_DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.CANNOT, "잠꼬대 심함"),
                createLifestyle(9L, members.get("박지훈"), SleepTimeType._2200, WakeUpTimeType._0700, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.FREQUENT, ShowerTimeType.EVENING, null, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.HIGH, ColdToleranceType.LOW, null, VisitHomeFrequencyType.RARELY, LateNightSnackType.SOMETIMES, SnackInRoomType.ALLOWED, PhoneSoundType.EARPHONES, PerfumeUsageType.OFTEN, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.NONE, null, "울음소리 큼"),
                createLifestyle(10L, members.get("강수진"), SleepTimeType.BEFORE_2100, WakeUpTimeType.BEFORE_0600, SleepingHabitType.SNORING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.REGULAR, ShowerTimeType.MORNING, ShowerDurationType._20, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.MEDIUM, null, VisitHomeFrequencyType.EVERY_WEEK, null, SnackInRoomType.NOT_ALLOWED, null, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, null, null),
                createLifestyle(11L, members.get("이민호"), SleepTimeType._0100, WakeUpTimeType._0800, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.NONE, null, null, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.HIGH, MBTIType.ENTP, VisitHomeFrequencyType.EVERY_2_3_MONTHS, LateNightSnackType.OFTEN, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.SOMETIMES, StudyLocationType.OFF_DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.EXPERT, "장난침"),
                createLifestyle(12L, members.get("김하늘"), SleepTimeType._2200, WakeUpTimeType._0700, SleepingHabitType.SNORING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.FREQUENT, ShowerTimeType.MORNING, ShowerDurationType._25, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, MBTIType.ENFJ, VisitHomeFrequencyType.RARELY, LateNightSnackType.NEVER, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.VARIABLE, PerfumeUsageType.SOMETIMES, null, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.SMALL_ONLY, "웃음소리 큼"),
                createLifestyle(13L, members.get("박철수"), SleepTimeType._2400, WakeUpTimeType._0600, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.NONE, ShowerTimeType.EVENING, null, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ISTP, null, LateNightSnackType.SOMETIMES, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.SOMETIMES, StudyLocationType.DORMITORY, ExamPreparationType.NONE, ExerciseType.GYM, InsectToleranceType.CANNOT, "말싸움"),
                createLifestyle(14L, members.get("홍서연"), SleepTimeType.BEFORE_2100, WakeUpTimeType._0800, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.FREQUENT, ShowerTimeType.MORNING, ShowerDurationType._10, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.MEDIUM, ColdToleranceType.MEDIUM, null, VisitHomeFrequencyType.EVERY_2_3_MONTHS, LateNightSnackType.NEVER, SnackInRoomType.ALLOWED, null, PerfumeUsageType.SOMETIMES, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.EXPERT, "취침 후에 말 많이 함"),
                createLifestyle(15L, members.get("최민수"), SleepTimeType._0300, WakeUpTimeType._0600, SleepingHabitType.TEETH_GRINDING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.NONE, ShowerTimeType.MORNING, null, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, MBTIType.ENTJ, VisitHomeFrequencyType.RARELY, LateNightSnackType.SOMETIMES, SnackInRoomType.NOT_ALLOWED, null, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, null, "소란 피움"),
                createLifestyle(16L, members.get("유리"), SleepTimeType.AFTER_0300, WakeUpTimeType._0700, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.NONE, ShowerTimeType.EVENING, ShowerDurationType._25, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.HIGH, MBTIType.INFP, VisitHomeFrequencyType.EVERY_2_3_MONTHS, LateNightSnackType.SOMETIMES, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.NONE, StudyLocationType.VARIABLE, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.EXPERT, "잠꼬대 심함"),
                createLifestyle(17L, members.get("민혁"), SleepTimeType._0300, WakeUpTimeType._0600, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.REGULAR, ShowerTimeType.MORNING, ShowerDurationType._20, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ISFJ, VisitHomeFrequencyType.EVERY_MONTH, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, PhoneSoundType.EARPHONES, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.CANNOT, "울음소리 큼"),
                createLifestyle(18L, members.get("민수"), SleepTimeType._2400, WakeUpTimeType._0800, SleepingHabitType.TEETH_GRINDING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.OCCASIONAL, null, ShowerDurationType._25, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, MBTIType.INTP, VisitHomeFrequencyType.RARELY, LateNightSnackType.NEVER, SnackInRoomType.ALLOWED, null, PerfumeUsageType.OFTEN, null, ExamPreparationType.NONE, ExerciseType.GYM, InsectToleranceType.CANNOT, null),
                createLifestyle(19L, members.get("소희"), SleepTimeType._0300, WakeUpTimeType._1100, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.NONE, null, ShowerDurationType._15, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.HIGH, MBTIType.ESTJ, null, LateNightSnackType.SOMETIMES, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.NONE, null, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.SMALL_ONLY, "소리지름"),
                createLifestyle(20L, members.get("진수"), SleepTimeType.AFTER_0300, WakeUpTimeType._0600, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.REGULAR, ShowerTimeType.MORNING, ShowerDurationType._20, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ISFJ, VisitHomeFrequencyType.EVERY_MONTH, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, null, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, null, "술 먹고 춤 춤"),
                createLifestyle(21L, members.get("현주"), SleepTimeType._2200, WakeUpTimeType._0700, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.FREQUENT, ShowerTimeType.MORNING, ShowerDurationType._25, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.HIGH, null, null, LateNightSnackType.OFTEN, SnackInRoomType.NOT_ALLOWED, PhoneSoundType.VARIABLE, PerfumeUsageType.NONE, StudyLocationType.VARIABLE, ExamPreparationType.PREPARING, ExerciseType.GYM, null, "장난침"),
                createLifestyle(22L, members.get("진혁"), SleepTimeType._2400, WakeUpTimeType._0800, SleepingHabitType.TEETH_GRINDING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.OCCASIONAL, ShowerTimeType.EVENING, ShowerDurationType._25, CleaningFrequencyType.ALL_AT_ONCE, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, null, null, LateNightSnackType.NEVER, SnackInRoomType.ALLOWED, PhoneSoundType.SPEAKER, PerfumeUsageType.OFTEN, StudyLocationType.OFF_DORMITORY, ExamPreparationType.NONE, ExerciseType.GYM, InsectToleranceType.CANNOT, "말싸움"),
                createLifestyle(23L, members.get("가영"), SleepTimeType.BEFORE_2100, WakeUpTimeType._0700, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.REGULAR, null, ShowerDurationType._20, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ISFJ, null, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, PhoneSoundType.EARPHONES, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.CANNOT, null),
                createLifestyle(24L, members.get("민재"), SleepTimeType._0300, WakeUpTimeType._0800, SleepingHabitType.SLEEP_TALKING, SleepingSensitivityType.DARK, SmokingType.NON_SMOKER, DrinkingFrequencyType.NONE, null, ShowerDurationType._10, CleaningFrequencyType.OCCASIONALLY, HeatToleranceType.MEDIUM, ColdToleranceType.HIGH, MBTIType.ENFJ, null, LateNightSnackType.SOMETIMES, SnackInRoomType.NOT_ALLOWED, null, PerfumeUsageType.NONE, StudyLocationType.VARIABLE, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.SMALL_ONLY, "소란 피움")
//                createLifestyle(25L, members.get("서진"), SleepTimeType.AFTER_0300, WakeUpTimeType._0600, SleepingHabitType.SNORING, SleepingSensitivityType.LIGHT, SmokingType.SMOKER, DrinkingFrequencyType.REGULAR, null, ShowerDurationType._20, CleaningFrequencyType.IMMEDIATELY, HeatToleranceType.HIGH, ColdToleranceType.LOW, MBTIType.ISFJ, null, LateNightSnackType.OFTEN, SnackInRoomType.ALLOWED, PhoneSoundType.EARPHONES, PerfumeUsageType.NONE, StudyLocationType.DORMITORY, ExamPreparationType.PREPARING, ExerciseType.GYM, InsectToleranceType.CANNOT, null)
        );
    }

    public static List<PreferenceOrder> createDummyPreferenceOrders(Map<String, Member> members) {
        return Arrays.asList(
                createPreferenceOrder(1L, members.get("홍길동"), SleepTimeType._2200, WakeUpTimeType._0600, SleepingHabitType.SNORING, HeatToleranceType.MEDIUM),
                createPreferenceOrder(2L, members.get("김영희"), SmokingType.NON_SMOKER, DrinkingFrequencyType.OCCASIONAL, CleaningFrequencyType.IMMEDIATELY, SleepingSensitivityType.LIGHT),
                createPreferenceOrder(3L, members.get("이철수"), PerfumeUsageType.SOMETIMES, ExamPreparationType.PREPARING, ColdToleranceType.HIGH, SleepTimeType._2300),
                createPreferenceOrder(4L, members.get("박민수"), SleepTimeType._2400, WakeUpTimeType._0800, SleepingHabitType.TEETH_GRINDING, ColdToleranceType.LOW),
                createPreferenceOrder(5L, members.get("정수진"), SleepingSensitivityType.DARK, PerfumeUsageType.OFTEN, DrinkingFrequencyType.REGULAR, WakeUpTimeType._0900),
                createPreferenceOrder(6L, members.get("오세진"), SmokingType.SMOKER, SleepingHabitType.SLEEP_TALKING, CleaningFrequencyType.OCCASIONALLY, WakeUpTimeType._0700),
                createPreferenceOrder(7L, members.get("이정환"), SleepTimeType.BEFORE_2100, WakeUpTimeType.BEFORE_0600, SleepingHabitType.SNORING, ColdToleranceType.HIGH),
                createPreferenceOrder(8L, members.get("김민지"), SleepingSensitivityType.DARK, DrinkingFrequencyType.NONE, HeatToleranceType.MEDIUM, WakeUpTimeType._1000),
                createPreferenceOrder(9L, members.get("박지훈"), PerfumeUsageType.NONE, ExamPreparationType.PREPARING, CleaningFrequencyType.ALL_AT_ONCE, SleepTimeType._0300),
                createPreferenceOrder(10L, members.get("강수진"), SleepTimeType._2100, WakeUpTimeType.AFTER_1200, SleepingHabitType.SNORING, ColdToleranceType.MEDIUM),
                createPreferenceOrder(11L, members.get("이민호"), DrinkingFrequencyType.FREQUENT, PerfumeUsageType.SOMETIMES, HeatToleranceType.HIGH, WakeUpTimeType._0600),
                createPreferenceOrder(12L, members.get("김하늘"), ExamPreparationType.PREPARING, SleepingHabitType.TEETH_GRINDING, WakeUpTimeType._0600, HeatToleranceType.LOW),
                createPreferenceOrder(13L, members.get("박철수"), SleepTimeType._2200, DrinkingFrequencyType.OCCASIONAL, SleepingSensitivityType.DARK, ColdToleranceType.LOW),
                createPreferenceOrder(14L, members.get("홍서연"), SleepTimeType._2400, WakeUpTimeType._0900, SleepingHabitType.NONE, CleaningFrequencyType.ALL_AT_ONCE),
                createPreferenceOrder(15L, members.get("최민수"), PerfumeUsageType.SOMETIMES, SleepTimeType._0100, WakeUpTimeType._0600, DrinkingFrequencyType.REGULAR),
                createPreferenceOrder(16L, members.get("유리"), SleepingSensitivityType.LIGHT, HeatToleranceType.LOW, ColdToleranceType.HIGH, WakeUpTimeType.AFTER_1200),
                createPreferenceOrder(17L, members.get("민혁"), SleepingHabitType.SNORING, ExamPreparationType.NONE, PerfumeUsageType.OFTEN, DrinkingFrequencyType.FREQUENT),
                createPreferenceOrder(18L, members.get("민수"), HeatToleranceType.HIGH, ColdToleranceType.LOW, SleepTimeType._2100, WakeUpTimeType._0800),
                createPreferenceOrder(19L, members.get("소희"), SleepingSensitivityType.DARK, DrinkingFrequencyType.OCCASIONAL, SmokingType.NON_SMOKER, PerfumeUsageType.SOMETIMES),
                createPreferenceOrder(20L, members.get("진수"), CleaningFrequencyType.ALL_AT_ONCE, SleepingSensitivityType.LIGHT, WakeUpTimeType._0900, SleepingHabitType.SLEEP_TALKING),
                createPreferenceOrder(21L, members.get("현주"), SleepTimeType._2400, WakeUpTimeType._0700, ColdToleranceType.MEDIUM, HeatToleranceType.LOW),
                createPreferenceOrder(22L, members.get("진혁"), SleepTimeType._2200, PerfumeUsageType.OFTEN, DrinkingFrequencyType.REGULAR, SleepingHabitType.TEETH_GRINDING),
                createPreferenceOrder(23L, members.get("가영"), SleepingSensitivityType.DARK, ColdToleranceType.HIGH, SleepTimeType._2100, WakeUpTimeType._0600),
//                createPreferenceOrder(24L, members.get("민재"), SleepingHabitType.SLEEP_TALKING, HeatToleranceType.LOW, ColdToleranceType.MEDIUM, WakeUpTimeType._0800),
                createPreferenceOrder(25L, members.get("서진"), ExamPreparationType.NONE, SmokingType.SMOKER, PerfumeUsageType.NONE, DrinkingFrequencyType.OCCASIONAL)
        );
    }

    public static List<Candidate> createDummyCandidates(Recommendation recommendation, Map<String, Member> members) {
        return Arrays.asList(
                new Candidate(recommendation, members.get("서진"), 10d),
                new Candidate(recommendation, members.get("민재"), 50d),
                new Candidate(recommendation, members.get("가영"), 40d),
                new Candidate(recommendation, members.get("진혁"), 2d),
                new Candidate(recommendation, members.get("현주"), 95d),
                new Candidate(recommendation, members.get("진수"), 45d),
                new Candidate(recommendation, members.get("소희"), 20d),
                new Candidate(recommendation, members.get("민수"), 85d),
                new Candidate(recommendation, members.get("민혁"), 90d),
                new Candidate(recommendation, members.get("유리"), 65d)
        );
    }

    public static Recommendation createDummyRecommendation(Long id, Member member) {
        Recommendation recommendation = new Recommendation(member, LocalDateTime.now());
        ReflectionTestUtils.setField(recommendation, "id", id);
        return recommendation;
    }

    private static Member createMember(Long id, String name, String nickname, String email, String studentNumber,
                                       CollegeType collegeType, DepartmentType departmentType, MemberDormitoryType dormitoryType,
                                       LocalDate birthDate, GenderType genderType, String profileUrl, String studentCardImageUrl,
                                       Integer followerCount, Integer followingCount, boolean isRoommateMatched, RoleType authority) {
        Member member = Member.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .studentNumber(studentNumber)
                .collegeType(collegeType)
                .departmentType(departmentType)
                .dormitoryType(dormitoryType)
                .birthDate(birthDate)
                .genderType(genderType)
                .profileUrl(profileUrl)
                .studentCardImageUrl(studentCardImageUrl)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .isRoommateMatched(isRoommateMatched)
                .authority(authority)
                .build();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    private static Article createArticle(Long id, Member member, String title, String content, String thumbnailUrl, StatusType status, ArticleDormitoryType dormitoryType, BoardType boardType, String tags, int commentCount, int viewCount, int wishCount) {
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .status(status)
                .dormitoryType(dormitoryType)
                .boardType(boardType)
                .tags(tags)
                .commentCount(commentCount)
                .viewCount(viewCount)
                .wishCount(wishCount)
                .build();
        ReflectionTestUtils.setField(article, "id", id);
        return article;
    }

    private static Lifestyle createLifestyle(Long id, Member member, SleepTimeType sleepTimeType, WakeUpTimeType wakeUpTimeType, SleepingHabitType sleepingHabitType, SleepingSensitivityType sleepingSensitivityType, SmokingType smokingType, DrinkingFrequencyType drinkingFrequencyType, ShowerTimeType showerTimeType, ShowerDurationType showerDurationType, CleaningFrequencyType cleaningFrequencyType, HeatToleranceType heatToleranceType, ColdToleranceType coldToleranceType, MBTIType mbtiType, VisitHomeFrequencyType visitHomeFrequencyType, LateNightSnackType lateNightSnackType, SnackInRoomType snackInRoomType, PhoneSoundType phoneSoundType, PerfumeUsageType perfumeUsageType, StudyLocationType studyLocationType, ExamPreparationType examPreparationType, ExerciseType exerciseType, InsectToleranceType insectToleranceType, String drunkHabit) {
        Lifestyle lifestyle = Lifestyle.builder()
                .member(member)
                .sleepTimeType(sleepTimeType)
                .wakeUpTimeType(wakeUpTimeType)
                .sleepingHabitType(sleepingHabitType)
                .sleepingSensitivityType(sleepingSensitivityType)
                .smokingType(smokingType)
                .drinkingFrequencyType(drinkingFrequencyType)
                .showerTimeType(showerTimeType)
                .showerDurationType(showerDurationType)
                .cleaningFrequencyType(cleaningFrequencyType)
                .heatToleranceType(heatToleranceType)
                .coldToleranceType(coldToleranceType)
                .mbtiType(mbtiType)
                .visitHomeFrequencyType(visitHomeFrequencyType)
                .lateNightSnackType(lateNightSnackType)
                .snackInRoomType(snackInRoomType)
                .phoneSoundType(phoneSoundType)
                .perfumeUsageType(perfumeUsageType)
                .studyLocationType(studyLocationType)
                .examPreparationType(examPreparationType)
                .exerciseType(exerciseType)
                .insectToleranceType(insectToleranceType)
                .drunkHabit(drunkHabit)
                .build();
        ReflectionTestUtils.setField(lifestyle, "id", id);
        return lifestyle;
    }

    private static PreferenceOrder createPreferenceOrder(Long id, Member member, Enum<?> firstPreferenceOrder, Enum<?> secondPreferenceOrder, Enum<?> thirdPreferenceOrder, Enum<?> fourthPreferenceOrder) {
        PreferenceOrder preferenceOrder = PreferenceOrder.builder()
                .member(member)
                .firstPreferenceOrder(firstPreferenceOrder)
                .secondPreferenceOrder(secondPreferenceOrder)
                .thirdPreferenceOrder(thirdPreferenceOrder)
                .fourthPreferenceOrder(fourthPreferenceOrder)
                .build();
        ReflectionTestUtils.setField(preferenceOrder, "id", id);
        return preferenceOrder;
    }
}