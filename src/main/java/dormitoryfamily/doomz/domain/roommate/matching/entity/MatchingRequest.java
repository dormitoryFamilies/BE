package dormitoryfamily.doomz.domain.roommate.matching.entity;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = @UniqueConstraint(
        name = "uk_matching_request_member_pair",
        columnNames = {"member_low_id", "member_high_id"}
    ),
    indexes = @Index(
        name = "idx_matching_request_member_pair",
        columnList = "member_low_id, member_high_id"
    )
)
public class MatchingRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Column(name = "member_low_id", nullable = false)
    private Long memberLowId;

    @Column(name = "member_high_id", nullable = false)
    private Long memberHighId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Builder
    public MatchingRequest(Member sender, Member receiver, RequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status != null ? status : RequestStatus.PENDING;

        // memberLowId, memberHighId 자동 설정
        Long senderId = sender.getId();
        Long receiverId = receiver.getId();

        if (senderId.compareTo(receiverId) < 0) {
            this.memberLowId = senderId;
            this.memberHighId = receiverId;
        } else {
            this.memberLowId = receiverId;
            this.memberHighId = senderId;
        }
    }

    public static MatchingRequest createMatchingRequest(Member sender, Member receiver) {
        return MatchingRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestStatus.PENDING)
                .build();
    }

}
