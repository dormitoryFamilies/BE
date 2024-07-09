package dormitoryfamily.doomz.domain.member.dto.response;

import java.util.List;

public record MemberProfileListResponseDto (
        List<MemberProfileResponseDto> memberProfiles
){
    public static MemberProfileListResponseDto toDto(List<MemberProfileResponseDto> memberProfiles){
        return new MemberProfileListResponseDto(memberProfiles);
    }
}
