package dormitoryfamily.doomz.domain.article.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.article.util.SortType;
import dormitoryfamily.doomz.domain.member.entity.Member;
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
    public Slice<Article> findAllByDormitoryTypeAndBoardType(ArticleDormitoryType dormitoryType,
                                                             BoardType boardType,
                                                             ArticleRequest request,
                                                             Pageable pageable) {

        List<Article> content = queryFactory
                .selectFrom(article)
                .where(
                        dormitoryTypeEq(dormitoryType),
                        articleStatus(request),
                        boardTypeEx(boardType)
                )
                .orderBy(article.createdAt.desc())
                .orderBy(getOrderByExpression(request.sort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return createSlice(content, pageable);
    }

    private BooleanExpression dormitoryTypeEq(ArticleDormitoryType dormitoryType) {
        if (dormitoryType != null) {
            return article.dormitoryType.eq(dormitoryType);
        }
        return null;
    }

    private BooleanExpression articleStatus(ArticleRequest request) {
        if (request.status() != null) {
            StatusType statusType = StatusType.fromDescription(request.status());
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

    private Slice<Article> createSlice(List<Article> content, Pageable pageable) {
        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<Article> findMyArticleByDormitoryTypeAndBoardType(Member member,
                                                                   ArticleDormitoryType dormitoryType,
                                                                   BoardType boardType,
                                                                   Pageable pageable
    ) {
        List<Article> content = queryFactory
                .selectFrom(article)
                .where(
                        writerEq(member),
                        dormitoryTypeEq(dormitoryType),
                        boardTypeEx(boardType)
                )
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return createSlice(content, pageable);
    }

    private BooleanExpression writerEq(Member member) {
        if (member != null) {
            return article.member.eq(member);
        }
        return null;
    }

    @Override
    public Slice<Article> searchArticles(ArticleDormitoryType dormitoryType, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(article.dormitoryType.eq(dormitoryType))
                .andAnyOf(
                        article.title.containsIgnoreCase(keyword),
                        article.content.containsIgnoreCase(keyword),
                        article.tags.containsIgnoreCase(keyword)
                );

        List<Article> content = queryFactory
                .selectFrom(article)
                .where(builder)
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return createSlice(content, pageable);
    }
}
