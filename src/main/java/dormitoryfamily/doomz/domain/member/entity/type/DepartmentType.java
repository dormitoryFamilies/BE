package dormitoryfamily.doomz.domain.member.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidDepartmentTypeException;
import dormitoryfamily.doomz.domain.member.exception.MismatchedCollegeException;
import lombok.Getter;

@Getter
public enum DepartmentType {
    KOREAN_LITERATURE("국어국문학과", CollegeType.HUMANITIES),
    CHINESE_LITERATURE("중어중문학과", CollegeType.HUMANITIES),
    ENGLISH_LITERATURE("영어영문학과", CollegeType.HUMANITIES),
    GERMAN_LANGUAGE_CULTURE("독일언어문화학과", CollegeType.HUMANITIES),
    FRENCH_LANGUAGE_CULTURE("프랑스언어문화학과", CollegeType.HUMANITIES),
    RUSSIAN_LANGUAGE_CULTURE("러시아언어문화학과", CollegeType.HUMANITIES),
    PHILOSOPHY("철학과", CollegeType.HUMANITIES),
    HISTORY("사학과", CollegeType.HUMANITIES),
    ARCHAEOLOGY_ART_HISTORY("고고미술사학과", CollegeType.HUMANITIES),

    SOCIOLOGY("사회학과", CollegeType.SOCIAL_SCIENCES),
    PSYCHOLOGY("심리학과", CollegeType.SOCIAL_SCIENCES),
    PUBLIC_ADMINISTRATION("행정학과", CollegeType.SOCIAL_SCIENCES),
    INTERNATIONAL_RELATIONS("정치외교학과", CollegeType.SOCIAL_SCIENCES),
    ECONOMICS("경제학과", CollegeType.SOCIAL_SCIENCES),

    MATHEMATICS("수학과", CollegeType.NATURAL_SCIENCES),
    STATISTICS("정보통계학과", CollegeType.NATURAL_SCIENCES),
    PHYSICS("물리학과", CollegeType.NATURAL_SCIENCES),
    CHEMISTRY("화학과", CollegeType.NATURAL_SCIENCES),
    BIOLOGY("생물학과", CollegeType.NATURAL_SCIENCES),
    MICROBIOLOGY("미생물학과", CollegeType.NATURAL_SCIENCES),
    BIOCHEMISTRY("생화학과", CollegeType.NATURAL_SCIENCES),
    ASTRONOMY("천문우주학과", CollegeType.NATURAL_SCIENCES),
    EARTH_ENVIRONMENTAL_SCIENCE("지구환경과학과", CollegeType.NATURAL_SCIENCES),

    BUSINESS_ADMINISTRATION("경영학부", CollegeType.BUSINESS),
    INTERNATIONAL_BUSINESS("국제경영학과", CollegeType.BUSINESS),
    BUSINESS_INFORMATION("경영정보학과", CollegeType.BUSINESS),

    CIVIL_ENGINEERING("토목공학부", CollegeType.ENGINEERING),
    MECHANICAL_ENGINEERING("기계공학부", CollegeType.ENGINEERING),
    CHEMICAL_ENGINEERING("화학공학과", CollegeType.ENGINEERING),
    MATERIALS_ENGINEERING("신소재공학과", CollegeType.ENGINEERING),
    ARCHITECTURAL_ENGINEERING("건축공학과", CollegeType.ENGINEERING),
    SAFETY_ENGINEERING("안전공학과", CollegeType.ENGINEERING),
    ENVIRONMENTAL_ENGINEERING("환경공학과", CollegeType.ENGINEERING),
    INDUSTRIAL_CHEMISTRY("공업화학과", CollegeType.ENGINEERING),
    URBAN_ENGINEERING("도시공학과", CollegeType.ENGINEERING),
    ARCHITECTURE("건축학과", CollegeType.ENGINEERING),
    TECHNO_INDUSTRIAL_ENGINEERING("테크노산업공학과", CollegeType.ENGINEERING),

    ELECTRICAL_ENGINEERING("전기공학부", CollegeType.ELECTRONICS_INFORMATION),
    ELECTRONIC_ENGINEERING("전자공학부", CollegeType.ELECTRONICS_INFORMATION),
    SEMICONDUCTOR_ENGINEERING("반도체공학부", CollegeType.ELECTRONICS_INFORMATION),
    TELECOMMUNICATION_ENGINEERING("정보통신공학부", CollegeType.ELECTRONICS_INFORMATION),
    COMPUTER_SCIENCE("컴퓨터공학과", CollegeType.ELECTRONICS_INFORMATION),
    SOFTWARE_ENGINEERING("소프트웨어학부", CollegeType.ELECTRONICS_INFORMATION),
    INTELLIGENT_ROBOT_ENGINEERING("지능로봇공학과", CollegeType.ELECTRONICS_INFORMATION),

    FORESTRY("산림학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    REGIONAL_CONSTRUCTION_ENGINEERING("지역건설공학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    BIOSYSTEMS_ENGINEERING("바이오시스템공학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    WOOD_PAPER_SCIENCE("목재종이과학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    AGRICULTURAL_ECONOMICS("농업경제학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    PLANT_RESOURCES("식물자원학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    ENVIRONMENTAL_LIFE_CHEMISTRY("환경생명화학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    ANIMAL_SCIENCE("축산학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    FOOD_LIFE_ENGINEERING("식품생명공학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    SPECIALTY_CROPS("특용식물학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    HORTICULTURE("원예과학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),
    PLANT_MEDICINE("식물의학과", CollegeType.AGRICULTURE_LIFE_ENVIRONMENT),

    EDUCATION("교육학과", CollegeType.EDUCATION),
    KOREAN_EDUCATION("국어교육과", CollegeType.EDUCATION),
    ENGLISH_EDUCATION("영어교육과", CollegeType.EDUCATION),
    HISTORY_EDUCATION("역사교육과", CollegeType.EDUCATION),
    GEOGRAPHY_EDUCATION("지리교육과", CollegeType.EDUCATION),
    SOCIAL_EDUCATION("사회교육과", CollegeType.EDUCATION),
    ETHICS_EDUCATION("윤리교육과", CollegeType.EDUCATION),
    PHYSICS_EDUCATION("물리교육과", CollegeType.EDUCATION),
    CHEMISTRY_EDUCATION("화학교육과", CollegeType.EDUCATION),
    BIOLOGY_EDUCATION("생물교육과", CollegeType.EDUCATION),
    EARTH_SCIENCE_EDUCATION("지구과학교육과", CollegeType.EDUCATION),
    MATHEMATICS_EDUCATION("수학교육과", CollegeType.EDUCATION),
    PHYSICAL_EDUCATION("체육교육과", CollegeType.EDUCATION),

    FOOD_NUTRITION("식품영양학과", CollegeType.HUMAN_ECOSCIENCES),
    CHILD_WELFARE("아동복지학과", CollegeType.HUMAN_ECOSCIENCES),
    CLOTHING("의류학과", CollegeType.HUMAN_ECOSCIENCES),
    HOUSING_ENVIRONMENT("주거환경학과", CollegeType.HUMAN_ECOSCIENCES),
    CONSUMER_SCIENCE("소비자학과", CollegeType.HUMAN_ECOSCIENCES),

    VETERINARY_PREMED("수의예과", CollegeType.VETERINARY_MEDICINE),
    VETERINARY_SCIENCE("수의학과", CollegeType.VETERINARY_MEDICINE),

    PHARMACY("약학과", CollegeType.PHARMACY),
    PHARMACEUTICAL_SCIENCE("제약학과", CollegeType.PHARMACY),

    MEDICAL_PRACTICE("의예과", CollegeType.MEDICINE),
    MEDICINE("의학과", CollegeType.MEDICINE),
    NURSING("간호학과", CollegeType.MEDICINE),

    RADIATION_FUSION("방사광융합학과", CollegeType.BIOHEALTH_SHARED),
    PHARMACEUTICAL_BIOTECHNOLOGY("제약바이오학과", CollegeType.BIOHEALTH_SHARED),
    NATURAL_MATERIALS("천연물소재학과", CollegeType.BIOHEALTH_SHARED),
    COSMETIC_INDUSTRY("화장품산업학과", CollegeType.BIOHEALTH_SHARED),

    SELF_DEVELOPMENT_MAJOR("자율전공학부", CollegeType.SELF_DEVELOPMENT_MAJOR),

    FINE_ARTS("조형예술학과", CollegeType.CONVERGENCE_DEPARTMENTS),
    DESIGN("디자인학과", CollegeType.CONVERGENCE_DEPARTMENTS),

    BIOHEALTH("바이오헬스학부", CollegeType.BIOHEALTH);

    private final String description;
    private final CollegeType college;

    DepartmentType(String description, CollegeType college) {
        this.description = description;
        this.college = college;
    }

    public static DepartmentType fromDescription(String description, CollegeType collegeType) {
        for (DepartmentType departmentType : DepartmentType.values()) {
            if (departmentType.description.equals(description)) {
                validateCollegeForDepartment(departmentType, collegeType);
                return departmentType;
            }
        }
        throw new InvalidDepartmentTypeException();
    }

    private static void validateCollegeForDepartment(DepartmentType department, CollegeType college){
        if(!department.college.equals(college)){
            throw new MismatchedCollegeException();
        }
    }
}