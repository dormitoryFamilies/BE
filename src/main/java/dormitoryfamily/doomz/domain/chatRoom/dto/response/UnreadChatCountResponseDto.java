package dormitoryfamily.doomz.domain.chatRoom.dto.response;

public record UnreadChatCountResponseDto(
        int totalCount
) {
    public static UnreadChatCountResponseDto from(int totalCount) {
        return new UnreadChatCountResponseDto(totalCount);
    }
}
