package dormitoryfamily.doomz.domain.roomate.entity;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;
import dormitoryfamily.doomz.global.converter.EnumConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferenceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_lifestyle_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private LifestyleType lifestyleType;

    @Convert(converter = EnumConverter.class)
    private Enum<?> lifestyleDetail;

    private Integer preferenceOrder;

    @Builder
    public PreferenceOrder(Member member,
                           LifestyleType lifestyleType,
                           Enum<?> lifestyleDetail,
                           Integer preferenceOrder) {
        this.member = member;
        this.lifestyleType = lifestyleType;
        this.lifestyleDetail = lifestyleDetail;
        this.preferenceOrder = preferenceOrder;
    }
}
