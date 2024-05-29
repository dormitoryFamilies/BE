package dormitoryfamily.doomz.global.util;

import jakarta.validation.constraints.NotBlank;

public record SearchRequestDto (
        @NotBlank(message = "검색어를 입력해주세요")
        String q
){
}
