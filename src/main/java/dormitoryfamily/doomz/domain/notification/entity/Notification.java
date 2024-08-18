package dormitoryfamily.doomz.domain.notification.entity;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Entity
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = CASCADE)
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @OnDelete(action = CASCADE)
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 200)
    private NotificationType notificationType;

    @Column(nullable = false)
    private boolean isRead;

    private String articleTitle;
    private Long targetId;

    @Builder
    public Notification(
            Member receiver,
            Member sender,
            NotificationType notificationType,
            boolean isRead,
            String articleTitle,
            Long targetId
    ) {
        this.receiver = receiver;
        this.sender = sender;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.articleTitle = articleTitle;
        this.targetId = targetId;
    }

    public void setReadStatusToTrue() {
        isRead = true;
    }
}
