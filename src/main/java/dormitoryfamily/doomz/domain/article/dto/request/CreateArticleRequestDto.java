package dormitoryfamily.doomz.domain.article.dto.request;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.util.List;

public record CreateArticleRequestDto(
        String dormitoryType,
        String boardType,
        String title,
        String content,
        String tags,
        List<String> imagesUrls
) {
    public static Article toEntity(Member member, CreateArticleRequestDto requestDto) {
        return Article.builder()
                .member(member)
                .title(requestDto.title)
                .content(requestDto.content)
                .thumbnailUrl(requestDto.imagesUrls.get(0))
                .status(StatusType.PROGRESS)
                .dormitoryType(ArticleDormitoryType.fromName(requestDto.dormitoryType))
                .boardType(BoardType.fromDescription(requestDto.boardType))
                .tags(requestDto.tags)
                .build();
    }
}
