package dormitoryfamily.doomz.domain.roommate.event;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;

public record LifestyleIndexEvent(
        Member member,
        Lifestyle lifestyle
) {
    public static LifestyleIndexEvent of(Member member, Lifestyle lifestyle) {
        return new LifestyleIndexEvent(member, lifestyle);
    }
}
