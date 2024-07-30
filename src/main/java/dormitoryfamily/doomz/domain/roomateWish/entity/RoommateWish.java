package dormitoryfamily.doomz.domain.roomateWish.entity;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoommateWish extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roommate_wish_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wisher_id")
    private Member wisher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wished_id")
    private Member wished;

    @Builder
    public RoommateWish(Member wisher, Member wished) {
        this.wisher = wisher;
        this.wished = wished;
    }

    public static RoommateWish createRoommateWish(Member wisher, Member wished){
        return RoommateWish.builder()
                .wisher(wisher)
                .wished(wished)
                .build();
    }
}
