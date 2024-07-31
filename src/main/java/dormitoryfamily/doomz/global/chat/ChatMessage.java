package dormitoryfamily.doomz.global.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatroom.entity.ChatRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ChatMessage implements Serializable {

    private String roomUUID;
    private Long senderId;
    private String message;

    public static Chat toEntity(ChatMessage chatMessage, ChatRoom chatRoom){
        return Chat.builder()
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .chatRoom(chatRoom)
                .build();
    }
}
