package dormitoryfamily.doomz.domain.chatRoom.entity;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatRoomStatus;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "room_uuid")
    private String roomUUID;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;  //최초에 채팅으 보낸 사람

    @ManyToOne
    @JoinColumn(name = "reciever_id")
    private Member receiver;  //최초에 채팅을 받은 사람

    private int senderUnreadCount;

    private int receiverUnreadCount;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public ChatRoom(String roomUUID,
                    Member sender,
                    Member receiver,
                    int senderUnreadCount,
                    int receiverUnreadCount,
                    ChatRoomStatus chatRoomStatus
    ) {
        this.roomUUID = roomUUID;
        this.sender = sender;
        this.receiver = receiver;
        this.senderUnreadCount = senderUnreadCount;
        this.receiverUnreadCount = receiverUnreadCount;
        this.chatRoomStatus = chatRoomStatus;
    }

    public static ChatRoom create(Member sender, Member receiver) {
        return ChatRoom.builder()
                .roomUUID(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .senderUnreadCount(0)
                .receiverUnreadCount(0)
                .chatRoomStatus(ChatRoomStatus.BOTH)
                .build();
    }

    public void deleteSender() {
        this.senderUnreadCount = 0;
        this.chatRoomStatus = ChatRoomStatus.ONLY_RECEIVER;
    }

    public void deleteReceiver() {
        this.receiverUnreadCount = 0;
        this.chatRoomStatus = ChatRoomStatus.ONLY_SENDER;
    }

    public void changeChatRoomStatus(ChatRoomStatus chatRoomStatus){
        this.chatRoomStatus = chatRoomStatus;
    }
}

