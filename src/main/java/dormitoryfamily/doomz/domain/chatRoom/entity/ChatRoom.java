package dormitoryfamily.doomz.domain.chatRoom.entity;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private int senderUnreadCount;
    private boolean senderIsDeleted;

    @ManyToOne
    @JoinColumn(name = "reciever_id")
    private Member receiver;  //최초에 채팅을 받은 사람

    private int receiverUnreadCount;
    private boolean receiverIsDeleted;

    private String latestText;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Chat> chatList = new ArrayList<>();

}
