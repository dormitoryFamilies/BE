package dormitoryfamily.doomz.domain.replyComment.service;

import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.comment.service.CommentService;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replyComment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.replyComment.exception.ReplyCommentNotExistsException;
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
    private final CommentService commentService;

    public CreateReplyCommentResponseDto saveReplyComment(Member member, Long commentId, CreateReplyCommentRequestDto requestDto) {
        Comment comment = getCommentById(commentId);
        checkCommentIsDeleted(comment);
        ReplyComment replyComment = CreateReplyCommentRequestDto.toEntity(member, comment, requestDto);
        replyCommentRepository.save(replyComment);
        comment.getArticle().increaseCommentCount();
        return CreateReplyCommentResponseDto.fromEntity(replyComment);
    }

    private Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    private void checkCommentIsDeleted(Comment comment) {
        if (comment.isDeleted()) {
            throw new CommentIsDeletedException();
        }
    }

    public void removeReplyComment(Member member, Long replyCommentId) {
        ReplyComment replyComment = getReplyCommentById(replyCommentId);
        replyCommentRepository.delete(replyComment);
        commentService.decideCommentDeletion(replyComment.getComment());
        replyComment.getComment().getArticle().decreaseCommentCount();
    }

    private ReplyComment getReplyCommentById(Long replyCommentId){
        return replyCommentRepository.findById(replyCommentId)
                .orElseThrow(ReplyCommentNotExistsException::new);
    }
}

