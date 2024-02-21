package dormitoryfamily.doomz.domain.comment.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
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
import dormitoryfamily.doomz.domain.replyComment.repository.ReplyCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;

    public CreateCommentResponseDto saveComment(Long articleId, Member loginMember, CreateCommentRequestDto requestDto) {
        Article article = getArticleById(articleId);
        Comment comment = CreateCommentRequestDto.toEntity(loginMember, article, requestDto);
        commentRepository.save(comment);
        article.increaseCommentCount();
        return CreateCommentResponseDto.fromEntity(comment);
    }

    public CommentListResponseDto getCommentList(Long articleId, Member loginMember) {
        Article article = getArticleById(articleId);
        List<Comment> comments = commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId);
        List<CommentResponseDto> commentResponseDto = comments.stream()
                .map(comment -> CommentResponseDto.fromEntity(loginMember, article, comment))
                .collect(toList());
        return CommentListResponseDto.toDto(article.getCommentCount(), commentResponseDto);
    }

    public void removeComment(Member loginMember, Long commentId) {
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

    private void checkAlreadyDeleted(Comment comment){
        if (comment.isDeleted()) {
            throw new CommentIsDeletedException();
        }
    }

    private void isWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    private Article getArticleById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    public void decideCommentDeletion(Comment comment){;
        if(comment.isDeleted()&&!hasReplyComment(comment.getId())){
            commentRepository.delete(comment);
        }
    }

    private boolean hasReplyComment(Long commentId) {
        return replyCommentRepository.findFirstByCommentId(commentId).isPresent();
    }
}

