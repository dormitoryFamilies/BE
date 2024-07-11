package dormitoryfamily.doomz.domain.comment.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

import java.util.List;

public record CommentListResponseDto(
        long loginMemberId,
        int totalCount,
        List<CommentResponseDto> comments
) {
    public static CommentListResponseDto from(Member loginMember, int totalCount, List<CommentResponseDto> comments) {
        return new CommentListResponseDto(loginMember.getId(), totalCount, comments);
    }
}
