package dormitoryfamily.doomz.domain.roommate.matching.util;

import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingRequestTypeNotExitsException;

public enum StatusType{

    SENT("sent"),
    RECEIVED("received");

    private final String statusType;

    StatusType(String statusType) {
        this.statusType = statusType;
    }

    public static StatusType fromString(String statusType) {
        for (StatusType type : StatusType.values()) {
            if (type.statusType.equals(statusType)) {
                return type;
            }
        }
        throw new MatchingRequestTypeNotExitsException();
    }
}
