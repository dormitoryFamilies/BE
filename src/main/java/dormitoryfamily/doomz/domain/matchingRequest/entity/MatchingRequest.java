package dormitoryfamily.doomz.domain.matchingRequest.entity;

import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;  //최초에 채팅을 보낸 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;  //최초에 채팅을 받은 사람

    @Builder
    public MatchingRequest(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public static MatchingRequest createMatchingRequest(Member sender, Member receiver){
        return MatchingRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
