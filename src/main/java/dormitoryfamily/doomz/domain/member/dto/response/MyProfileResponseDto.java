package dormitoryfamily.doomz.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDate;


public record MyProfileResponseDto (
        Long memberId,
        String name,
        String genderType,
        String nickname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        String memberDormitoryType,
        String collegeType,
        String departmentType,
        String studentNumber,
        String profileUrl
){
    public static MyProfileResponseDto fromEntity(Member member){
        return new MyProfileResponseDto(
                member.getId(),
                member.getName(),
                member.getGenderType().getDescription(),
                member.getNickname(),
                member.getBirthDate(),
                member.getDormitoryType().getDescription(),
                member.getCollegeType().getDescription(),
                member.getDepartmentType().getDescription(),
                member.getStudentNumber(),
                member.getProfileUrl()
        );
    }
}
