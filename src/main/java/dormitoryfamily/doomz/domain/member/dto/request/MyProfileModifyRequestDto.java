package dormitoryfamily.doomz.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MyProfileModifyRequestDto(
        String nickname,
        String memberDormitoryType,
        String collegeType,
        String departmentType,

        @Size(min = 10, max = 10, message = "학번은 10자리여야 합니다.")
        @NotBlank(message = "공백은 유효하지 않습니다.")
        @Pattern(regexp = "^[^\\s].*", message = "학번의 앞부분에 공백이 올 수 없습니다.")
        String studentNumber,

        String profileUrl
){

}
