package dormitoryfamily.doomz.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ArticleResponseDto(
        Long articleId,
        Long memberId,
        String nickname,
        String profileUrl,
        String memberDormitory,
        String articleDormitory,
        String boardType,
        String tags,
        String title,
        String content,
        int wishCount,
        boolean isWished,
        boolean isWriter,
        String status,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        List<String> imagesUrls
) {
    public static ArticleResponseDto fromEntity(Member loginMember, Article article, boolean isWished, boolean isWriter, List<ArticleImage> articleImages) {
        return new ArticleResponseDto(
                article.getId(),
                article.getMember().getId(),
                article.getMember().getNickname(),
                article.getMember().getProfileUrl(),
                article.getMember().getDormitoryType().getDescription(),
                article.getDormitoryType().getName(),
                article.getBoardType().getDescription(),
                article.getTags(),
                article.getTitle(),
                article.getContent(),
                article.getWishCount(),
                isWished,
                isWriter,
                article.getStatus().getDescription(),
                article.getCreatedAt(),
                getArticleImagesUrls(articleImages)
        );
    }

    private static List<String> getArticleImagesUrls(List<ArticleImage> images) {
        return images.stream().map(ArticleImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
