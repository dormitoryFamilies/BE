package dormitoryfamily.doomz.domain.wish.dto;

import java.util.List;

public record WishListResponseDto(
        List<WishResponseDto> members
) {
    public static WishListResponseDto toDto(List<WishResponseDto> members) {
        return new WishListResponseDto(members);
    }
}
