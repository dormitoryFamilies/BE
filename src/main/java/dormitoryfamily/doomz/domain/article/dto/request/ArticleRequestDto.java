package dormitoryfamily.doomz.domain.article.dto.request;

import dormitoryfamily.doomz.domain.article.dto.validation.MaxListSize;
import dormitoryfamily.doomz.domain.article.dto.validation.TagCountConstraint;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ArticleRequestDto(
        String dormitoryType,
        String boardType,

        @Length(min = 1, max = 20, message = "제목은 최소 1자, 최대 20자 입니다.")
        @NotBlank(message = "공백은 유효하지 않습니다.")
        String title,

        @Length(min = 1, max = 500, message = "본문은 최소 1자, 최대 500자 입니다.")
        @NotBlank(message = "공백은 유효하지 않습니다.")
        String content,

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
