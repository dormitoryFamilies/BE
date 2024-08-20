package dormitoryfamily.doomz.domain.board.replycomment.service;

import dormitoryfamily.doomz.domain.board.comment.entity.Comment;
import dormitoryfamily.doomz.domain.board.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.board.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.board.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.board.comment.service.CommentService;
import dormitoryfamily.doomz.domain.board.replycomment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.board.replycomment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.board.replycomment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.board.replycomment.event.ReplyCommentCreatedEvent;
import dormitoryfamily.doomz.domain.board.replycomment.exception.ReplyCommentNotExistsException;
import dormitoryfamily.doomz.domain.board.replycomment.repository.ReplyCommentRepository;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.ARTICLE_REPLY_COMMENT;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyCommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentService commentService;
    private final ApplicationEventPublisher eventPublisher;

    public CreateReplyCommentResponseDto saveReplyComment(PrincipalDetails principalDetails,
                                                          Long commentId,
                                                          CreateReplyCommentRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Comment comment = getCommentById(commentId);

        checkCommentIsDeleted(comment);
        ReplyComment replyComment = CreateReplyCommentRequestDto.toEntity(loginMember, comment, requestDto);
        replyCommentRepository.save(replyComment);

        comment.getArticle().increaseCommentCount();
        //알림 전송
        notifySavingReplyCommentInfo(replyComment, comment, loginMember);

        return CreateReplyCommentResponseDto.fromEntity(replyComment);
    }

    private void notifySavingReplyCommentInfo(ReplyComment replyComment, Comment comment, Member loginMember) {
        if (!Objects.equals(loginMember.getId(), comment.getMember().getId())) {
            eventPublisher.publishEvent(new ReplyCommentCreatedEvent(replyComment, comment.getArticle(), ARTICLE_REPLY_COMMENT));
        }
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    private void checkCommentIsDeleted(Comment comment) {
        if (comment.isDeleted()) {
            throw new CommentIsDeletedException();
        }
    }

    public void removeReplyComment(PrincipalDetails principalDetails, Long replyCommentId) {
        Member loginMember = principalDetails.getMember();
        ReplyComment replyComment = getReplyCommentById(replyCommentId);

        isWriter(loginMember, replyComment.getMember());
        replyCommentRepository.delete(replyComment);

        replyComment.getComment().getArticle().decreaseCommentCount();

        commentService.deleteCommentIfIsDeletedAndNoReplyComments(replyComment.getComment());
    }

    private ReplyComment getReplyCommentById(Long replyCommentId) {
        return replyCommentRepository.findById(replyCommentId)
                .orElseThrow(ReplyCommentNotExistsException::new);
    }

    private void isWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }
}

