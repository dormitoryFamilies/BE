package dormitoryfamily.doomz.domain.comment.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CreateCommentResponseDto saveComment(Long articleId, Member member, CreateCommentRequestDto requestDto) {
        Article article = getArticleById(articleId);
        Comment comment = CreateCommentRequestDto.toEntity(member, article, requestDto);
        commentRepository.save(comment);
        article.increaseCommentCount();
        return CreateCommentResponseDto.fromEntity(comment);
    }

    public void removeComment(Member member, Long commentId) {
        Comment comment = getCommentById(commentId);
        if (hasReplyComment(comment)) {
            comment.markAsDeleted();
        } else {
            commentRepository.delete(comment);
        }
        comment.getArticle().decreaseCommentCount();
    }

    public void decideCommentDeletion(Comment comment){;
        if(comment.isDeleted()&&hasReplyComment(comment)){
            commentRepository.delete(comment);
        }
    }

    private boolean hasReplyComment(Comment comment) {
        return comment.getReplyComments().stream().anyMatch(reply -> true);
    }

    private Article getArticleById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }
}

