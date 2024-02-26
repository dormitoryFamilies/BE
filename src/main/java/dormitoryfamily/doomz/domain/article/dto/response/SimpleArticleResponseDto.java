package dormitoryfamily.doomz.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.article.entity.Article;

import java.time.LocalDateTime;

public record SimpleArticleResponseDto(
        Long articleId,
        String nickName,
        String profileUrl,
        String boardType,
        String title,
        String content,
        int CommentCount,
        int viewCount,
        int wishCount,
        boolean isWished,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH-mm-ss")
        LocalDateTime createdAt,
        String thumbnailUrl
) {
        public static SimpleArticleResponseDto fromEntity(Article article, boolean isWished) {
                return new SimpleArticleResponseDto(
                        article.getId(),
                        article.getMember().getNickname(),
                        article.getMember().getProfileUrl(),
                        article.getBoardType().getDescription(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCommentCount(),
                        article.getWishCount(),
                        article.getViewCount(),
                        isWished,
                        article.getStatus().getDescription(),
                        article.getCreatedAt(),
                        article.getThumbnailUrl()
                );
        }
}
