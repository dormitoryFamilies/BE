package dormitoryfamily.doomz.domain.member.dto.request;

public record MyProfileModifyRequestDto(
        String nickname,
        String memberDormitoryType,
        String profileUrl
) {

}
