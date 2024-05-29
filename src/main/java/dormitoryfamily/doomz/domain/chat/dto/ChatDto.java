package dormitoryfamily.doomz.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dormitoryfamily.doomz.domain.chat.entity.Chat;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatDto(
        Long chatId,
        Long senderId,
        String message,
        String imageUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime sentTime
) implements Serializable {

    public static ChatDto fromEntity(Chat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getSenderId(),
                chat.getMessage(),
                chat.getImageUrl(),
                chat.getCreatedAt()
        );
    }
}