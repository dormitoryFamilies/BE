package dormitoryfamily.doomz.domain.chatting.chat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;
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
@Table(indexes = @Index(name = "idx_message_id", columnList = "messageId", unique = true))
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(unique = true)
    private String messageId;

    private Long senderId;

    private String message;

    @ManyToOne
    @JoinColumn(name = "room_uuid", referencedColumnName = "room_uuid")
    private ChatRoom chatRoom;

    @Builder
    public Chat(String messageId, Long senderId, String message, ChatRoom chatRoom) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
