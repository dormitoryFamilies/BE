package dormitoryfamily.doomz.domain.member.follow.dto;

public record FollowStatusResponseDto(
        boolean isFollowing
) {
    public static FollowStatusResponseDto from(boolean isFollowing) {
        return new FollowStatusResponseDto(isFollowing);
    }
}
