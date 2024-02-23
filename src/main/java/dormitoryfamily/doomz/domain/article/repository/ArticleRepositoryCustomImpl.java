package dormitoryfamily.doomz.domain.article.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.article.util.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static dormitoryfamily.doomz.domain.article.entity.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Article> findAllByDormitoryTypeAndBoardType(ArticleDormitoryType dormitoryType, BoardType boardType, ArticleRequest request, Pageable pageable) {

        List<Article> content = queryFactory
                .selectFrom(article)
                .where(
                        dormitoryTypeEq(dormitoryType),
                        articleStatus(request),
                        boardTypeEx(boardType)
                )
                .orderBy(getOrderByExpression(request.sort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression dormitoryTypeEq(ArticleDormitoryType dormitoryType) {
        if (dormitoryType != null) {
            return article.dormitoryType.eq(dormitoryType);
        }
        return null;
    }

    private BooleanExpression articleStatus(ArticleRequest request) {
        StatusType statusType = StatusType.fromDescription(request.status());
        if (request.status() != null) {
            return article.status.eq(statusType);
        }
        return null;
    }

    private BooleanExpression boardTypeEx(BoardType boardType) {
        if (boardType != null) {
            return article.boardType.eq(boardType);
        }
        return null;
    }

    private OrderSpecifier<?>[] getOrderByExpression(String sortType) {
        return SortType.fromString(sortType).getOrderSpecifiers();
    }
}