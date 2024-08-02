package dormitoryfamily.doomz.domain.chatting.chat.util;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;

import static dormitoryfamily.doomz.domain.chatting.chat.entity.QChat.chat;

@Getter
public enum SortType {

    LATEST("latest", chat.createdAt.desc()),
    OLDEST("oldest", chat.createdAt.asc());

    private final String sortType;
    private final OrderSpecifier<?> orderSpecifier;

    SortType(String sortType, OrderSpecifier<?> orderSpecifier) {
        this.sortType = sortType;
        this.orderSpecifier = orderSpecifier;
    }

    public static SortType fromString(String sortType) {
        for (SortType type : SortType.values()) {
            if (type.sortType.equals(sortType)) {
                return type;
            }
        }
        return LATEST;
    }
}
