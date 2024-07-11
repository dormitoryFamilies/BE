package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import org.springframework.data.domain.Page;

import java.util.List;

public record MemberProfilePagingListResponseDto(
        int totalPageNumber,
        int nowPageNumber,
        boolean isLast,
        List<? extends MemberBasePagingResponseDto> memberProfiles
) {
    public static MemberProfilePagingListResponseDto from(Page<Follow> follows, List<? extends MemberBasePagingResponseDto> memberProfiles){
        return new MemberProfilePagingListResponseDto(
                follows.getTotalPages(),
                follows.getNumber(),
                follows.isLast(),
                memberProfiles);
    }
}
