package dormitoryfamily.doomz.domain.member.entity;

import dormitoryfamily.doomz.domain.member.entity.type.CollegeType;
import dormitoryfamily.doomz.domain.member.entity.type.DepartmentType;
import dormitoryfamily.doomz.domain.member.entity.type.GenderType;
import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
//setter와 기본생성자 임시 허용
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String nickname;
    private String studentNumber;

    @Enumerated(EnumType.STRING)
    private CollegeType collegeType;

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    @Enumerated(EnumType.STRING)
    private MemberDormitoryType dormitoryType;

    private LocalDate birthDate;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    private String profileUrl;
    private String studentCardImage;

    public Member(String name,
                  String nickname,
                  String studentNumber,
                  CollegeType collegeType,
                  DepartmentType departmentType,
                  MemberDormitoryType dormitoryType,
                  LocalDate birthDate,
                  String phoneNumber,
                  GenderType genderType,
                  String profileUrl,
                  String studentCardImage) {
        this.name = name;
        this.nickname = nickname;
        this.studentNumber = studentNumber;
        this.collegeType = collegeType;
        this.departmentType = departmentType;
        this.dormitoryType = dormitoryType;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.genderType = genderType;
        this.profileUrl = profileUrl;
        this.studentCardImage = studentCardImage;
    }
}
