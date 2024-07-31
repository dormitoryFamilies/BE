package dormitoryfamily.doomz.domain.chatting.chatroom.dto.response;

public record UnreadChatCountResponseDto(
        int totalCount
) {
    public static UnreadChatCountResponseDto from(int totalCount) {
        return new UnreadChatCountResponseDto(totalCount);
    }
}
