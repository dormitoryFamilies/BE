package dormitoryfamily.doomz.domain.board.replycomment.dto.response;

import dormitoryfamily.doomz.domain.board.replycomment.entity.ReplyComment;

public record CreateReplyCommentResponseDto(
        Long replyCommentId
) {
    public static CreateReplyCommentResponseDto fromEntity(ReplyComment replyComment) {
        return new CreateReplyCommentResponseDto(replyComment.getId());
    }
}
