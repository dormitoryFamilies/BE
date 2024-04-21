package dormitoryfamily.doomz.domain.chatRoom.dto.response;

public record UnreadChatCountResponseDto (
        int totalCount
){
    public static UnreadChatCountResponseDto toDto(int totalCount){
        return new UnreadChatCountResponseDto(totalCount);
    }
}
