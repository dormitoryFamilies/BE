package dormitoryfamily.doomz.domain.roomate.preference.entity;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
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

    @Convert(converter = EnumConverter.class)
    private Enum<?> firstPreferenceOrder;

    @Convert(converter = EnumConverter.class)
    private Enum<?> secondPreferenceOrder;

    @Convert(converter = EnumConverter.class)
    private Enum<?> thirdPreferenceOrder;

    @Convert(converter = EnumConverter.class)
    private Enum<?> fourthPreferenceOrder;

    @Builder
    public PreferenceOrder(Member member,
                           Enum<?> firstPreferenceOrder, Enum<?> secondPreferenceOrder,
                           Enum<?> thirdPreferenceOrder, Enum<?> fourthPreferenceOrder) {
        this.member = member;
        this.firstPreferenceOrder = firstPreferenceOrder;
        this.secondPreferenceOrder = secondPreferenceOrder;
        this.thirdPreferenceOrder = thirdPreferenceOrder;
        this.fourthPreferenceOrder = fourthPreferenceOrder;
    }

    public void updateOrder(Enum<?> firstPreferenceOrder, Enum<?> secondPreferenceOrder,
                            Enum<?> thirdPreferenceOrder, Enum<?> fourthPreferenceOrder) {
        this.firstPreferenceOrder = firstPreferenceOrder;
        this.secondPreferenceOrder = secondPreferenceOrder;
        this.thirdPreferenceOrder = thirdPreferenceOrder;
        this.fourthPreferenceOrder = fourthPreferenceOrder;
    }
}
