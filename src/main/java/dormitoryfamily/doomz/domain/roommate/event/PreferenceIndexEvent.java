package dormitoryfamily.doomz.domain.roommate.event;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;

public record PreferenceIndexEvent(
        Long memberId,
        PreferenceOrder preferenceOrder,
        Member member
) {
    public static PreferenceIndexEvent of(Long memberId, PreferenceOrder preferenceOrder, Member member) {
        return new PreferenceIndexEvent(memberId, preferenceOrder, member);
    }
}
