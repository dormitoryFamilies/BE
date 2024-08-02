package dormitoryfamily.doomz.domain.member.member.dto.request;

public record MyProfileModifyRequestDto(
        String nickname,
        String memberDormitoryType,
        String profileUrl
) {

}
