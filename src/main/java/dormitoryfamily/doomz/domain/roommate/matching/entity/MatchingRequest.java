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

    @Builder
    public MatchingRequest(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public static MatchingRequest createMatchingRequest(Member sender, Member receiver) {
        return MatchingRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
