package dormitoryfamily.doomz.domain.comment.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;


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

    public CommentListResponseDto getCommentList(Long articleId, Member member) {

        Article article = getArticleById(articleId);

        List<Comment> comments = commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId);

        List<CommentResponseDto> commentResponseDto = comments.stream()
                .map(comment -> CommentResponseDto.fromEntity(member, comment))
                .collect(toList());

        return CommentListResponseDto.toDto(article.getCommentCount(), commentResponseDto);
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

