package dormitoryfamily.doomz.domain.member.dto.response;

import java.util.List;

public record MemberProfileListResponseDto(
        List<? extends MemberBaseResponseDto> memberProfiles
){
    public static MemberProfileListResponseDto toDto(List<? extends MemberBaseResponseDto> memberProfiles){
        return new MemberProfileListResponseDto(memberProfiles);
    }
}
