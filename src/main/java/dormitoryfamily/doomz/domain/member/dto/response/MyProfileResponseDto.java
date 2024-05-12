package dormitoryfamily.doomz.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDate;


public record MyProfileResponseDto (
        String name,
        String gender,
        String nickname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        String memberDormitory,
        String college,
        String department,
        String studentNumber,
        String profileUrl
){
    public static MyProfileResponseDto fromEntity(Member member){
        return new MyProfileResponseDto(
                member.getName(),
                member.getGenderType().getDescription(),
                member.getNickname(),
                member.getBirthDate(),
                member.getDormitoryType().getName(),
                member.getCollegeType().getDescription(),
                member.getDepartmentType().getDescription(),
                member.getStudentNumber(),
                member.getProfileUrl()
        );
    }
}
