package dormitoryfamily.doomz.global.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chat.entity.Chat;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatEntity(
        Long senderId,
        String roomUUID,
        String message,
        String imageUrl,
        String sentTime
) implements Serializable {

    public static Chat toEntity(ChatEntity chatEntity){
        return Chat.builder()
                .roomUUID(chatEntity.roomUUID)
                .senderId(chatEntity.senderId())
                .message(chatEntity.message())
                .imageUrl(chatEntity.imageUrl())
                .build();
    }
}
