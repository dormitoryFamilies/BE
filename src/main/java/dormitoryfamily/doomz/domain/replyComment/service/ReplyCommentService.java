package dormitoryfamily.doomz.domain.replyComment.service;

import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.comment.service.CommentService;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.replyComment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replyComment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.replyComment.exception.ReplyCommentNotExistsException;
import dormitoryfamily.doomz.domain.replyComment.repository.ReplyCommentRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyCommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentService commentService;

    public CreateReplyCommentResponseDto saveReplyComment(PrincipalDetails principalDetails,
                                                          Long commentId,
                                                          CreateReplyCommentRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Comment comment = getCommentById(commentId);
        checkCommentIsDeleted(comment);
        ReplyComment replyComment = CreateReplyCommentRequestDto.toEntity(loginMember, comment, requestDto);
        replyCommentRepository.save(replyComment);
        comment.getArticle().increaseCommentCount();
        return CreateReplyCommentResponseDto.fromEntity(replyComment);
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
        commentService.decideCommentDeletion(replyComment.getComment());
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

