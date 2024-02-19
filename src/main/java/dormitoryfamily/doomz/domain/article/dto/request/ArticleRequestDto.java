package dormitoryfamily.doomz.domain.article.dto.request;

import dormitoryfamily.doomz.domain.article.dto.validation.MaxListSize;
import dormitoryfamily.doomz.domain.article.dto.validation.TagCountConstraint;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.util.List;

public record ArticleRequestDto(
        String dormitoryType,
        String boardType,
        String title, //최대 50자
        String content, //최대 1500자

        @TagCountConstraint
        String tags,

        @MaxListSize
        List<String> imagesUrls
) {
    public static Article toEntity(Member member, ArticleRequestDto requestDto) {
        return Article.builder()
                .member(member)
                .title(requestDto.title)
                .content(requestDto.content)
                .thumbnailUrl(!requestDto.imagesUrls.isEmpty() ? requestDto.imagesUrls.get(0) : null)
                .status(StatusType.PROGRESS)
                .dormitoryType(ArticleDormitoryType.fromName(requestDto.dormitoryType))
                .boardType(BoardType.fromDescription(requestDto.boardType))
                .tags(requestDto.tags)
                .build();
    }
}
