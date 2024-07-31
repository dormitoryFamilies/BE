package dormitoryfamily.doomz.domain.roommate.wish.dto.response;

public record RoommateWishStatusResponseDto (
    boolean isRoommateWished
){

    public static RoommateWishStatusResponseDto from(boolean isRoommateWished) {
        return new RoommateWishStatusResponseDto(isRoommateWished);
    }
}
