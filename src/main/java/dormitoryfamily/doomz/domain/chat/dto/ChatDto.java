package dormitoryfamily.doomz.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ChatDto implements Serializable {

    private Long chatId;
    private String roomUUID;
    private Long senderId;
    private String message;
    private String imageUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sentTime;

    @Builder
    public ChatDto(Long chatId, String roomUUID,
                   Long senderId, String message,
                   String imageUrl, LocalDateTime sentTime) {
        this.chatId = chatId;
        this.roomUUID = roomUUID;
        this.senderId = senderId;
        this.message = message;
        this.imageUrl = imageUrl;
        this.sentTime = sentTime;
    }

    public static Chat toEntity(ChatDto chatDto){
        return Chat.builder()
                .roomUUID(chatDto.getRoomUUID())
                .senderId(chatDto.getSenderId())
                .message(chatDto.getMessage())
                .imageUrl(chatDto.getImageUrl())
                .build();
    }

    public static ChatDto fromEntity(Chat chat){
        return ChatDto.builder()
                .chatId(chat.getId())
                .roomUUID(chat.getRoomUUID())
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .imageUrl(chat.getImageUrl())
                .sentTime(chat.getCreatedAt())
                .build();
    }

    public void setChatIdAndSentTime(Long id, LocalDateTime createdAt) {
        this.chatId = id;
        this.sentTime = createdAt;
    }
}
