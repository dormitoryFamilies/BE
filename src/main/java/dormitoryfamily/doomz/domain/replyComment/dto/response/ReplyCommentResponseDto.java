package dormitoryfamily.doomz.domain.replyComment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;

import java.time.LocalDateTime;

public record ReplyCommentResponseDto (
        Long replyCommentId,
        Long memberId,
        String profileUrl,
        String nickname,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH-mm-ss")
        LocalDateTime createdAt,
        String content,
        boolean isWriter
) {
        public static ReplyCommentResponseDto fromEntity(Member member, ReplyComment replyComment) {
                return new ReplyCommentResponseDto(
                        replyComment.getId(),
                        replyComment.getMember().getId(),
                        replyComment.getMember().getProfileUrl(),
                        replyComment.getMember().getNickname(),
                        replyComment.getCreatedAt(),
                        replyComment.getContent(),
                        false//추후 수정
                );
        }

}



