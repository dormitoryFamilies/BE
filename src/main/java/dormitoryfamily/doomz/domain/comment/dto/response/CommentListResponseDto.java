package dormitoryfamily.doomz.domain.comment.dto.response;

import java.util.List;

public record CommentListResponseDto (
    int totalCount,
    List<CommentResponseDto> comments
){
    public static CommentListResponseDto toDto(int totalCount, List<CommentResponseDto> comments) {
        return new CommentListResponseDto(totalCount, comments);
    }
}
