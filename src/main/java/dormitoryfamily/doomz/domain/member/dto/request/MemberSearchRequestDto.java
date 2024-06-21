package dormitoryfamily.doomz.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSearchRequestDto (
        @NotBlank(message = "검색어를 입력해주세요")
        String q
){
}
