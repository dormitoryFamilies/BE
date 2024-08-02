package dormitoryfamily.doomz.domain.board.article.util;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;

import static dormitoryfamily.doomz.domain.board.article.entity.QArticle.article;

@Getter
public enum SortType {
    CREATED_AT("createdAt", new OrderSpecifier[] {article.createdAt.desc()}),
    POPULARITY("popularity", new OrderSpecifier[] {article.wishCount.desc(), article.viewCount.desc(), article.createdAt.desc()});

    private final String sortType;
    private final OrderSpecifier<?>[] orderSpecifiers;

    SortType(String sortType, OrderSpecifier<?>[] orderSpecifiers) {
        this.sortType = sortType;
        this.orderSpecifiers = orderSpecifiers;
    }

    public static SortType fromString(String sortType) {
        for (SortType type : SortType.values()) {
            if (type.sortType.equals(sortType)) {
                return type;
            }
        }
        return CREATED_AT;
    }
}
