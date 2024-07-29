package dormitoryfamily.doomz.domain.matchingResult.entity;

import dormitoryfamily.doomz.domain.matchingRequest.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingResult {

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
    public MatchingResult(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public static MatchingResult createMatchingResult(Member sender, Member receiver){
        return MatchingResult.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
