package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidShowerDurationTypeException;
import lombok.Getter;

@Getter
public enum ShowerDurationType {

    _10("10분"),
    _20("20분"),
    _30("30분"),
    _40("40분"),
    _50("50분"),
    _60("60분");

    private final String description;

    ShowerDurationType(String description) {
        this.description = description;
    }

    public static ShowerDurationType fromDescription(String description) {
        if(description == null) {
            return null;
        }

        for (ShowerDurationType type : ShowerDurationType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidShowerDurationTypeException();
    }
}
