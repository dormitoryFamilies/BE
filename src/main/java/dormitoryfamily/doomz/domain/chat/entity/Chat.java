package dormitoryfamily.doomz.domain.chat.entity;

import dormitoryfamily.doomz.domain.chat.entity.type.VisibleStatus;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "room_uuid")
    private String roomUUID;

    private boolean isFromSender;

    private String message;

    private String imageUrl;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private VisibleStatus visible;

    @ManyToOne
    @JoinColumn(name = "chat_room_Id", referencedColumnName = "room_uuid", insertable = false, updatable = false)
    private ChatRoom chatRoom;
}
