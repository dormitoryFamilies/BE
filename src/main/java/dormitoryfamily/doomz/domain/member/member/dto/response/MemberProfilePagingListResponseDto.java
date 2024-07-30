package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.follow.entity.Follow;
import dormitoryfamily.doomz.domain.roomate.wish.entity.RoommateWish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MemberProfilePagingListResponseDto(
        Integer totalPageNumber,
        int nowPageNumber,
        boolean isLast,
        List<? extends MemberBaseResponseDto> memberProfiles
) {
    public static <T> MemberProfilePagingListResponseDto from(Page<T> pages, List<? extends MemberBaseResponseDto> memberProfiles) {
        return new MemberProfilePagingListResponseDto(
                pages.getTotalPages(),
                pages.getNumber(),
                pages.isLast(),
                memberProfiles);
    }

    public static MemberProfilePagingListResponseDto from(Slice<RoommateWish> wishes, List<? extends MemberBaseResponseDto> memberProfiles){
        return new MemberProfilePagingListResponseDto(
                null,
                wishes.getNumber(),
                wishes.isLast(),
                memberProfiles);
    }
}
