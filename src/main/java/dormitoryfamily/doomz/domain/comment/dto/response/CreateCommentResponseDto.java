package dormitoryfamily.doomz.domain.comment.dto.response;

import dormitoryfamily.doomz.domain.comment.entity.Comment;

public record CreateCommentResponseDto (
    Long commentId
) {
    public static CreateCommentResponseDto fromEntity(Comment comment) {
        return new CreateCommentResponseDto(comment.getId());
    }
}
