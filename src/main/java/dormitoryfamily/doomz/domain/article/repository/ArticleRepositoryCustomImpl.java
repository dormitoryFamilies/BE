package dormitoryfamily.doomz.domain.article.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.article.util.SortType;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static dormitoryfamily.doomz.domain.article.entity.QArticle.article;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Article> findAllByDormitoryType(ArticleDormitoryType dormitoryType, ArticleRequest request, Pageable pageable) {

        List<Article> content = queryFactory
                .selectFrom(article)
                .where(
                        dormitoryTypeEq(dormitoryType),
                        articleStatus(request)
                )
                .orderBy(getOrderByExpression(request.sort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new SliceImpl<>(content, pageable, true);
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

    private OrderSpecifier<?>[] getOrderByExpression(String sortType) {
        return SortType.fromString(sortType).getOrderSpecifiers();
    }
}
