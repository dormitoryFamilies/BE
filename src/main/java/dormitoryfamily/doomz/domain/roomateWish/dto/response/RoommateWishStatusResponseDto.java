package dormitoryfamily.doomz.domain.roomateWish.dto.response;

public record RoommateWishStatusResponseDto (
    boolean isRoommateWished
){

    public static RoommateWishStatusResponseDto from(boolean isRoommateWished) {
        return new RoommateWishStatusResponseDto(isRoommateWished);
    }
}
