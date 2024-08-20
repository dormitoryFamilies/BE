package dormitoryfamily.doomz.domain.board.comment.service;

import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.board.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.board.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.board.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.board.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.board.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.board.comment.dto.response.CommentListResponseDto;
import dormitoryfamily.doomz.domain.board.comment.dto.response.CommentResponseDto;
import dormitoryfamily.doomz.domain.board.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.board.comment.entity.Comment;
import dormitoryfamily.doomz.domain.board.comment.event.CommentCreatedEvent;
import dormitoryfamily.doomz.domain.board.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.board.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.board.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.board.replycomment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.board.replycomment.repository.ReplyCommentRepository;
import dormitoryfamily.doomz.domain.board.wish.entity.ArticleWish;
import dormitoryfamily.doomz.domain.board.wish.repository.ArticleWishRepository;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static dormitoryfamily.doomz.domain.board.article.entity.type.BoardType.ALL;
import static dormitoryfamily.doomz.domain.board.article.entity.type.BoardType.fromDescription;
import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.ARTICLE_COMMENT;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final ArticleWishRepository articleWishRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateCommentResponseDto saveComment(Long articleId, PrincipalDetails principalDetails, CreateCommentRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);

        Comment comment = CreateCommentRequestDto.toEntity(loginMember, article, requestDto);
        commentRepository.save(comment);
        article.increaseCommentCount();
        //알림 전송
        notifySavingCommentInfo(comment, article, loginMember);

        return CreateCommentResponseDto.fromEntity(comment);
    }

    private void notifySavingCommentInfo(Comment comment, Article article, Member loginMember) {
        if (!Objects.equals(loginMember.getId(), article.getMember().getId())) {
            eventPublisher.publishEvent(new CommentCreatedEvent(comment, article, ARTICLE_COMMENT));
        }
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    public CommentListResponseDto findCommentList(Long articleId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);

        List<Comment> comments = commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId);
        List<CommentResponseDto> commentResponseDto = comments.stream()
                .map(comment -> CommentResponseDto.fromEntity(article.getMember(), comment))
                .collect(toList());

        return CommentListResponseDto.from(loginMember, article.getCommentCount(), commentResponseDto);
    }

    public void removeComment(PrincipalDetails principalDetails, Long commentId) {
        Member loginMember = principalDetails.getMember();
        Comment comment = getCommentById(commentId);

        checkAlreadyDeleted(comment);
        validateIsWriter(loginMember, comment.getMember());

        if (checkHasReplyComments(comment.getId())) {
            comment.markAsDeleted();
        } else {
            commentRepository.delete(comment);
        }
        comment.getArticle().decreaseCommentCount();
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    private void checkAlreadyDeleted(Comment comment) {
        if (comment.isDeleted()) {
            throw new CommentIsDeletedException();
        }
    }

    private void validateIsWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    private boolean checkHasReplyComments(Long commentId) {
        return replyCommentRepository.existsByCommentId(commentId);
    }

    public void deleteCommentIfIsDeletedAndNoReplyComments(Comment comment) {
        if (comment.isDeleted() && !checkHasReplyComments(comment.getId())) {
            commentRepository.delete(comment);
        }
    }

    public ArticleListResponseDto findMyComments(PrincipalDetails principalDetails, String articleDormitoryType, String articleBoardType, ArticleRequest request, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);
        BoardType boardType = getBoardType(articleBoardType);

        List<Long> articleIds = getArticleIdsFromMyComments(loginMember.getId());

        Slice<Article> articles = articleRepository
                .findAllByIdInAndDormitoryTypeAndBoardType(articleIds, dormitoryType, boardType, request, pageable);

        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, createSimpleArticleResponseDtos(loginMember, articles));
    }

    private BoardType getBoardType(String articleBoardType) {
        BoardType boardType = fromDescription(articleBoardType);
        if (boardType.equals(ALL)) {
            boardType = null;
        }
        return boardType;
    }

    private List<Long> getArticleIdsFromMyComments(Long memberId) {
        List<Comment> myComments = commentRepository.findAllByMemberId(memberId);
        List<ReplyComment> myReplyComments = replyCommentRepository.findAllByMemberId(memberId);

        return getArticleIds(myComments, myReplyComments);
    }

    private List<Long> getArticleIds(List<Comment> comments, List<ReplyComment> replyComments) {
        return Stream.concat(
                        comments.stream().map(comment -> comment.getArticle().getId()),
                        replyComments.stream().map(replyComment -> replyComment.getComment().getArticle().getId())
                )
                .distinct()
                .toList();
    }

    private List<SimpleArticleResponseDto> createSimpleArticleResponseDtos(Member loginMember, Slice<Article> articles) {
        List<ArticleWish> memberWishes = articleWishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = isArticleWishedByMember(article, memberWishes);
                    return SimpleArticleResponseDto.fromEntity(article, isWished);
                })
                .toList();
    }

    private boolean isArticleWishedByMember(Article article, List<ArticleWish> wishes) {
        return wishes.stream()
                .anyMatch(wish -> wish.getArticle().getId().equals(article.getId()));
    }
}

