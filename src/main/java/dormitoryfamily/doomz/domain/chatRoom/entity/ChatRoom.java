package dormitoryfamily.doomz.domain.chatRoom.entity;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus.IN;
import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus.OUT;


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

    @CreatedDate
    private LocalDateTime senderEnteredAt;

    @CreatedDate
    private LocalDateTime receiverEnteredAt;

    private int senderUnreadCount;

    private int receiverUnreadCount;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus senderStatus;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus receiverStatus;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public ChatRoom(String roomUUID, Member sender, Member receiver,
                    LocalDateTime senderEnteredAt, LocalDateTime receiverEnteredAt,
                    int senderUnreadCount, int receiverUnreadCount,
                    ChatMemberStatus senderStatus, ChatMemberStatus receiverStatus) {
        this.roomUUID = roomUUID;
        this.sender = sender;
        this.receiver = receiver;
        this.senderEnteredAt = senderEnteredAt;
        this.receiverEnteredAt = receiverEnteredAt;
        this.senderUnreadCount = senderUnreadCount;
        this.receiverUnreadCount = receiverUnreadCount;
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

    public void reEnterSender() {
        this.senderEnteredAt = LocalDateTime.now();
    }

    public void reEnterReceiver() {
        this.receiverEnteredAt = LocalDateTime.now();
    }

    public void senderInChatRoom() {
        this.senderUnreadCount = 0;
        this.senderStatus = IN;
    }

    public void receiverInChatRoom() {
        this.receiverUnreadCount = 0;
        this.receiverStatus = IN;
    }

    public void setSenderStatusOut() {
        this.senderStatus = OUT;
    }

    public void setReceiverStatusOut() {
        this.receiverStatus = OUT;
    }

    public void increaseSenderUnreadCount() {
        this.senderUnreadCount += 1;
    }

    public void increaseReceiverUnreadCount() {
        this.receiverUnreadCount += 1;
    }

    public void deleteSender() {
        this.senderUnreadCount = 0;
        this.senderEnteredAt = null;
        this.senderStatus = OUT;
    }

    public void deleteReceiver() {
        this.receiverUnreadCount = 0;
        this.receiverEnteredAt = null;
        this.receiverStatus = OUT;
    }

    @PrePersist
    private void init() {
        this.senderUnreadCount = 0;
        this.receiverUnreadCount = 0;
        this.senderStatus = OUT;
        this.receiverStatus = OUT;
    }
}

