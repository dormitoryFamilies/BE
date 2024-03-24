package dormitoryfamily.doomz.domain.member.entity;

import dormitoryfamily.doomz.domain.member.entity.type.CollegeType;
import dormitoryfamily.doomz.domain.member.entity.type.DepartmentType;
import dormitoryfamily.doomz.domain.member.entity.type.GenderType;
import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Entity
@Getter
//setter와 기본생성자 임시 허용
@Setter
@NoArgsConstructor
@Slf4j
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String nickname;
    private String studentNumber;
    private String email; //OAuth 로그인 여부 확인용

    @Enumerated(EnumType.STRING)
    private CollegeType collegeType;

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    @Enumerated(EnumType.STRING)
    private MemberDormitoryType dormitoryType;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    private String profileUrl;
    private String studentCardImageUrl;
    private Integer followingCount;
    private Integer followerCount;

    @Builder
    public Member(String name,
                  String nickname,
                  String email,
                  String studentNumber,
                  CollegeType collegeType,
                  DepartmentType departmentType,
                  MemberDormitoryType dormitoryType,
                  LocalDate birthDate,
                  GenderType genderType,
                  String profileUrl,
                  String studentCardImageUrl,
                  Integer followingCount,
                  Integer followerCount) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.studentNumber = studentNumber;
        this.collegeType = collegeType;
        this.departmentType = departmentType;
        this.dormitoryType = dormitoryType;
        this.birthDate = birthDate;
        this.genderType = genderType;
        this.profileUrl = profileUrl;
        this.studentCardImageUrl = studentCardImageUrl;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public void increaseFollowingCount(){
        followingCount += 1;
    }

    public void decreaseFollowingCount(){
        followingCount -= 1;
    }

    public void increaseFollowerCount(){
        this.followerCount += 1;
    }

    public void decreaseFollowerCount(){
        this.followerCount -= 1;
    }

    @PrePersist
    private void init() {
        followingCount = 0;
        followerCount = 0;
    }

}
