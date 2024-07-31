package dormitoryfamily.doomz.domain.replycomment.dto.response;

import dormitoryfamily.doomz.domain.replycomment.entity.ReplyComment;

public record CreateReplyCommentResponseDto(
        Long replyCommentId
) {
    public static CreateReplyCommentResponseDto fromEntity(ReplyComment replyComment) {
        return new CreateReplyCommentResponseDto(replyComment.getId());
    }
}
