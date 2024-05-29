package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import org.springframework.data.domain.Page;

import java.util.List;

public record MemberProfilePagingListResponseDto(
        int totalPageNumber,
        int nowPageNumber,
        boolean isLast,
        List<MemberProfileResponseDto> memberProfiles
) {
    public static MemberProfilePagingListResponseDto toDto(Page<Follow> follows, List<MemberProfileResponseDto> memberProfiles){
        return new MemberProfilePagingListResponseDto(
                follows.getTotalPages(),
                follows.getNumber(),
                follows.isLast(),
                memberProfiles);
    }
}
