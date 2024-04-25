package dormitoryfamily.doomz.domain.member.entity;

import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
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

import java.time.LocalDate;

@Entity
@Getter
//setter와 기본생성자 임시 허용
@Setter
@NoArgsConstructor
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
    private boolean isSignUp;
    private boolean isAuthenticated;
    private String authority;

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
                  Integer followerCount,
                  String authority) {
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
        this.isSignUp = isSignUp;
        this.isAuthenticated = isAuthenticated;
        this.authority = authority;
    }

    public void setUpProfile(MemberSetUpProfileRequestDto requestDto) {
        this.nickname = requestDto.nickname();
        this.studentCardImageUrl = requestDto.studentCardImageUrl();
        this.collegeType = CollegeType.fromDescription(requestDto.collegeType());
        this.departmentType = DepartmentType.fromDescription(requestDto.departmentType(), collegeType);
        this.studentNumber = requestDto.studentNumber();
        this.dormitoryType = MemberDormitoryType.fromName(requestDto.dormitoryType());
        this.isSignUp = true;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public void increaseFollowingCount(){
        this.followingCount += 1;
    }

    public void decreaseFollowingCount(){
        this.followingCount -= 1;
    }

    public void increaseFollowerCount(){
        this.followerCount += 1;
    }

    public void decreaseFollowerCount(){
        this.followerCount -= 1;
    }

    public void updateProfile(MyProfileModifyRequestDto requestDto){
        this.nickname = requestDto.nickname();
        this.dormitoryType = MemberDormitoryType.fromDescription(requestDto.memberDormitoryType());
        this.profileUrl = requestDto.profileUrl();
    }

    @PrePersist
    private void init() {
        followingCount = 0;
        followerCount = 0;
    }

}
