package dormitoryfamily.doomz.domain.member.dto.response;

import java.util.List;

public record MemberInfoListResponseDto(
        List<MemberInfoResponseDto> memberProfiles
){
    public static MemberInfoListResponseDto toDto(List<MemberInfoResponseDto> memberProfiles){
        return new MemberInfoListResponseDto(memberProfiles);
    }
}
