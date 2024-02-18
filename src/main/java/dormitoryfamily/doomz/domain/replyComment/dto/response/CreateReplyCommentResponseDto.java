package dormitoryfamily.doomz.domain.replyComment.dto.response;

import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;

public record CreateReplyCommentResponseDto(
    Long replyCommentId
) {
    public static CreateReplyCommentResponseDto fromEntity(ReplyComment replyComment) {
        return new CreateReplyCommentResponseDto(replyComment.getId());
    }
}
