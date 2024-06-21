package dormitoryfamily.doomz.domain.chatRoom.entity;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus;
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

import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus.IN;
import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus.OUT;
import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatRoomStatus.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;  //최초에 채팅을 보낸 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;  //최초에 채팅을 받은 사람

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    private int senderUnreadCount;

    private int receiverUnreadCount;

    private Long lastReceiverOnlyChatId;

    private Long lastSenderOnlyChatId;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus senderStatus;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus receiverStatus;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public ChatRoom(String roomUUID, Member sender,
                    Member receiver, ChatRoomStatus chatRoomStatus,
                    int senderUnreadCount, int receiverUnreadCount,
                    Long lastReceiverOnlyChatId, Long lastSenderOnlyChatId,
                    ChatMemberStatus senderStatus, ChatMemberStatus receiverStatus) {
        this.roomUUID = roomUUID;
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoomStatus = chatRoomStatus;
        this.senderUnreadCount = senderUnreadCount;
        this.receiverUnreadCount = receiverUnreadCount;
        this.lastReceiverOnlyChatId = lastReceiverOnlyChatId;
        this.lastSenderOnlyChatId = lastSenderOnlyChatId;
        this.senderStatus = senderStatus;
        this.receiverStatus = receiverStatus;
    }

    public static ChatRoom create(Member sender, Member receiver) {
        return ChatRoom.builder()
                .roomUUID(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public void deleteSender(Long lastReceiverOnlyChatId) {
        this.senderUnreadCount = 0;
        this.chatRoomStatus = ONLY_RECEIVER;
        this.lastReceiverOnlyChatId = lastReceiverOnlyChatId;
        this.lastSenderOnlyChatId = null;
    }

    public void deleteReceiver(Long lastSenderOnlyChatId) {
        this.receiverUnreadCount = 0;
        this.chatRoomStatus = ONLY_SENDER;
        this.lastSenderOnlyChatId = lastSenderOnlyChatId;
        this.lastReceiverOnlyChatId = null;
    }

    public void changeChatRoomStatus(ChatRoomStatus chatRoomStatus){
        this.chatRoomStatus = chatRoomStatus;
    }

    public void setSenderStatusIn(){
        this.senderStatus  = IN;
    }

    public void setSenderStatusOut(){
        this.senderStatus  = OUT;
    }

    public void setReceiverStatusIn(){
        this.receiverStatus = IN;
    }

    public void setReceiverStatusOut(){
        this.receiverStatus = OUT;
    }

    public void increaseSenderUnreadCount(){
        this.senderUnreadCount +=1;
    }

    public void increaseReceiverUnreadCount(){
        this.receiverUnreadCount +=1;
    }

    public void resetSenderUnreadCount(){
        this.senderUnreadCount = 0;
    }

    public void resetReceiverUnreadCount(){
        this.receiverUnreadCount = 0;
    }

    @PrePersist
    private void init() {
       this.chatRoomStatus = BOTH;
       this.senderUnreadCount = 0;
       this.receiverUnreadCount = 0;
       this.lastSenderOnlyChatId= null;
       this.lastReceiverOnlyChatId = null;
       this.senderStatus = OUT;
       this.receiverStatus = OUT;
    }
}

