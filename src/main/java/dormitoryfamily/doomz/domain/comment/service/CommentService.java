package dormitoryfamily.doomz.domain.comment.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.replyComment.repository.ReplyCommentRepository;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final WishRepository wishRepository;

    public CreateCommentResponseDto saveComment(Long articleId, PrincipalDetails principalDetails, CreateCommentRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        Comment comment = CreateCommentRequestDto.toEntity(loginMember, article, requestDto);
        commentRepository.save(comment);
        article.increaseCommentCount();
        return CreateCommentResponseDto.fromEntity(comment);
    }

    public CommentListResponseDto getCommentList(Long articleId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        List<Comment> comments = commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId);
        List<CommentResponseDto> commentResponseDto = comments.stream()
                .map(comment -> CommentResponseDto.fromEntity(loginMember, comment))
                .collect(toList());
        return CommentListResponseDto.toDto(article.getCommentCount(), commentResponseDto);
    }

    public void removeComment(PrincipalDetails principalDetails, Long commentId) {
        Member loginMember = principalDetails.getMember();
        Comment comment = getCommentById(commentId);
        checkAlreadyDeleted(comment);
        isWriter(loginMember, comment.getMember());
        if (hasReplyComment(comment.getId())) {
            comment.markAsDeleted();
        } else {
            commentRepository.delete(comment);
        }
        comment.getArticle().decreaseCommentCount();
    }

    private void checkAlreadyDeleted(Comment comment) {
        if (comment.isDeleted()) {
            throw new CommentIsDeletedException();
        }
    }

    private void isWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    public void decideCommentDeletion(Comment comment) {
        if (comment.isDeleted() && !hasReplyComment(comment.getId())) {
            commentRepository.delete(comment);
        }
    }

    private boolean hasReplyComment(Long commentId) {
        return replyCommentRepository.existsByCommentId(commentId);
    }

    public ArticleListResponseDto findMyComments(PrincipalDetails principalDetails, String articleDormitoryType, String articleBoardType, ArticleRequest request, Pageable pageable ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        BoardType boardType;
        if(articleBoardType.equals("all")){
            boardType = null;
        }
        else{
            boardType = BoardType.fromDescription(articleBoardType);
        }

        List<Comment> myComments = commentRepository.findAllByMemberId(loginMember.getId());
        List<ReplyComment> myReplyComments = replyCommentRepository.findAllByMemberId(loginMember.getId());

        List<Long> articleIds = getArticleIds(myComments, myReplyComments);

        Slice<Article> articles = articleRepository
                .findAllByIdInAndDormitoryTypeAndBoardType(articleIds, dormitoryType, boardType, request, pageable);

        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    private List<Long> getArticleIds(List<Comment> comments, List<ReplyComment> replyComments) {
        return Stream.concat(
                        comments.stream().map(comment -> comment.getArticle().getId()),
                        replyComments.stream().map(replyComment -> replyComment.getComment().getArticle().getId())
                )
                .distinct()
                .toList();
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtos(Member loginMember, Slice<Article> articles) {
        List<Wish> memberWishes = wishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWishedByList(article, loginMember, memberWishes);
                    return SimpleArticleResponseDto.fromEntity(article, isWished);
                })
                .toList();
    }

    private boolean checkIfArticleIsWishedByList(Article article, Member loginMember, List<Wish> memberWishes) {
        return memberWishes.stream()
                .anyMatch(wish -> wish.getArticle().getId().equals(article.getId()));
    }
}

