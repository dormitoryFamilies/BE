package dormitoryfamily.doomz.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private Long senderId;

    private String message;

    @ManyToOne
    @JoinColumn(name = "room_uuid", referencedColumnName = "room_uuid")
    private ChatRoom chatRoom;

    @Builder
    public Chat(Long senderId, String message, ChatRoom chatRoom) {
        this.senderId = senderId;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
