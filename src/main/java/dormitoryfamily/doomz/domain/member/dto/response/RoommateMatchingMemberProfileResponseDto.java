package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record RoommateMatchingMemberProfileResponseDto(

        Long memberId,
        String nickname,
        String profileUrl,
        Integer followerCount,
        Integer followingCount,
        String name,
        String birthDate,
        String studentNumber,
        String departmentType

) {
    public static RoommateMatchingMemberProfileResponseDto fromEntity(Member member) {
        return new RoommateMatchingMemberProfileResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getFollowerCount(),
                member.getFollowingCount(),
                member.getName(),
                member.getBirthDate().toString(),
                member.getStudentNumber(),
                member.getDepartmentType().getDescription()
        );
    }
}
