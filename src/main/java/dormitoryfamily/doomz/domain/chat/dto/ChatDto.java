package dormitoryfamily.doomz.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDto implements Serializable {

    private Long senderId;
    private String roomUUID;
    private String message;
    private String imageUrl;

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sentTime;

    public static Chat toEntity(ChatDto chatDto){
        return Chat.builder()
                .roomUUID(chatDto.getRoomUUID())
                .senderId(chatDto.getSenderId())
                .message(chatDto.getMessage())
                .imageUrl(chatDto.getImageUrl())
                .build();
    }
}
