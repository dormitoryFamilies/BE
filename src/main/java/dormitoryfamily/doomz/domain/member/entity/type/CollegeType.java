package dormitoryfamily.doomz.domain.member.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import lombok.Getter;

@Getter
public enum CollegeType {
    HUMANITIES("인문대학"),
    SOCIAL_SCIENCES("사회과학대학"),
    NATURAL_SCIENCES("자연과학대학"),
    BUSINESS("경영대학"),
    ENGINEERING("공과대학"),
    ELECTRONICS_INFORMATION("전자정보대학"),
    AGRICULTURE_LIFE_ENVIRONMENT("농업생명환경대학"),
    EDUCATION("사범대학"),
    HUMAN_ECOSCIENCES("생활과학대학"),
    VETERINARY_MEDICINE("수의과대학"),
    PHARMACY("약학대학"),
    MEDICINE("의과대학"),
    BIOHEALTH_SHARED("바이오헬스공유대학"),
    SELF_DEVELOPMENT_MAJOR("자율전공학부"),
    CONVERGENCE_DEPARTMENTS("융합학과군"),
    BIOHEALTH("바이오헬스학부");

    private final String description;

    CollegeType(String description) {
        this.description = description;
    }

    public static CollegeType fromDescription(String description) {
        for (CollegeType type : CollegeType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidCollegeTypeException();
    }
}
