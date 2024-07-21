package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record StudentCardResponseDto(
        Long memberId,
        String name,
        String studentNumber,
        String Department,
        String studentCardUrl
) {
    public static StudentCardResponseDto fromEntity(Member member) {
        return new StudentCardResponseDto(
                member.getId(),
                member.getName(),
                member.getStudentNumber(),
                member.getDepartmentType().getDescription(),
                member.getStudentCardImageUrl()
        );
    }
}
