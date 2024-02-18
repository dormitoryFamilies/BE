package dormitoryfamily.doomz.domain.replyComment.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replyComment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.replyComment.repository.ReplyCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyCommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;

    public CreateReplyCommentResponseDto saveReplyComment(Member member, Long commentId, CreateReplyCommentRequestDto requestDto) {
        Comment comment = getCommentById(commentId);
        ReplyComment replyComment = CreateReplyCommentRequestDto.toEntity(member, comment, requestDto);
        ReplyComment savedReplyComment = replyCommentRepository.save(replyComment);
        return CreateReplyCommentResponseDto.fromEntity(savedReplyComment);
    }

    private Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }


}

