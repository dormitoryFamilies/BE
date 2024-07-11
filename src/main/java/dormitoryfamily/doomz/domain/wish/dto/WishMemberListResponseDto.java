package dormitoryfamily.doomz.domain.wish.dto;

import java.util.List;

public record WishMemberListResponseDto(
        List<WishMemberResponseDto> memberProfiles
) {
    public static WishMemberListResponseDto toDto(List<WishMemberResponseDto> members) {
        return new WishMemberListResponseDto(members);
    }
}
