package dormitoryfamily.doomz.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;

public record  ArticleSearchRequestDto (
    @NotBlank(message = "검색어를 입력해주세요")
    String q
){

}
