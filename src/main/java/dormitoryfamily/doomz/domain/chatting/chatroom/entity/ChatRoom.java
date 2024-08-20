package dormitoryfamily.doomz.domain.chatting.chatroom.entity;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.type.ChatMemberStatus;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
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


@Entity
@Getter
@Table(name = "chat_room", indexes = @Index(name = "idx_room_uuid", columnList = "room_uuid"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "room_uuid")
    private String roomUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private Member initiator;  //최초에 채팅을 보낸 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Member participant;  //최초에 채팅을 받은 사람

    @CreatedDate
    private LocalDateTime initiatorEnteredAt;

    @CreatedDate
    private LocalDateTime participantEnteredAt;

    private int initiatorUnreadCount;

    private int participantUnreadCount;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus initiatorStatus;

    @Enumerated(EnumType.STRING)
    private ChatMemberStatus participantStatus;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public ChatRoom(String roomUUID, Member initiator, Member participant,
                    LocalDateTime initiatorEnteredAt, LocalDateTime participantEnteredAt,
                    int initiatorUnreadCount, int participantUnreadCount,
                    ChatMemberStatus initiatorStatus, ChatMemberStatus participantStatus) {
        this.roomUUID = roomUUID;
        this.initiator = initiator;
        this.participant = participant;
        this.initiatorEnteredAt = initiatorEnteredAt;
        this.participantEnteredAt = participantEnteredAt;
        this.initiatorUnreadCount = initiatorUnreadCount;
        this.participantUnreadCount = participantUnreadCount;
        this.initiatorStatus = initiatorStatus;
        this.participantStatus = participantStatus;
    }

    public static ChatRoom create(Member initiator, Member participant) {
        return ChatRoom.builder()
                .roomUUID(UUID.randomUUID().toString())
                .initiator(initiator)
                .participant(participant)
                .build();
    }

    public void reEnterInitiator() {
        this.initiatorEnteredAt = LocalDateTime.now();
    }

    public void reEnterParticipant() {
        this.participantEnteredAt = LocalDateTime.now();
    }

    public void initiatorInChatRoom() {
        this.initiatorUnreadCount = 0;
        this.initiatorStatus = ChatMemberStatus.IN;
    }

    public void participantInChatRoom() {
        this.participantUnreadCount = 0;
        this.participantStatus = ChatMemberStatus.IN;
    }

    public void setInitiatorStatusOut() {
        this.initiatorStatus = ChatMemberStatus.OUT;
    }

    public void setParticipantStatusOut() {
        this.participantStatus = ChatMemberStatus.OUT;
    }

    public void increaseInitiatorUnreadCount() {
        this.initiatorUnreadCount += 1;
    }

    public void increaseParticipantUnreadCount() {
        this.participantUnreadCount += 1;
    }

    public void deleteInitiator() {
        this.initiatorUnreadCount = 0;
        this.initiatorEnteredAt = null;
        this.initiatorStatus = ChatMemberStatus.OUT;
    }

    public void deleteParticipant() {
        this.participantUnreadCount = 0;
        this.participantEnteredAt = null;
        this.participantStatus = ChatMemberStatus.OUT;
    }

    public boolean isMemberOutOfChatRoom(Long receiverId) {
        if (initiator.getId().equals(receiverId)) {
            return initiatorStatus == ChatMemberStatus.OUT;
        } else {
            return participantStatus == ChatMemberStatus.OUT;
        }
    }

    @PrePersist
    private void init() {
        this.initiatorUnreadCount = 0;
        this.participantUnreadCount = 0;
        this.initiatorStatus = ChatMemberStatus.OUT;
        this.participantStatus = ChatMemberStatus.OUT;
    }
}

