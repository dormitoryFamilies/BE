package dormitoryfamily.doomz.domain.member.entity;

import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.entity.type.*;
import dormitoryfamily.doomz.domain.member.exception.NotVisitorRoleException;
import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Lifestyle lifeStyle;

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

    @Enumerated(EnumType.STRING)
    private RoleType authority;

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
        this.authority = RoleType.ROLE_VISITOR;
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

    public void updateProfile(MyProfileModifyRequestDto requestDto) {
        this.nickname = requestDto.nickname();
        this.dormitoryType = MemberDormitoryType.fromDescription(requestDto.memberDormitoryType());
        this.profileUrl = requestDto.profileUrl();
    }

    public void setUpProfile(MemberSetUpProfileRequestDto requestDto) {
        if (authority != RoleType.ROLE_VISITOR) {
            throw new NotVisitorRoleException();
        }
        this.nickname = requestDto.nickname();
        this.studentCardImageUrl = requestDto.studentCardImageUrl();
        this.collegeType = CollegeType.fromDescription(requestDto.collegeType());
        this.departmentType = DepartmentType.fromDescription(requestDto.departmentType(), collegeType);
        this.studentNumber = requestDto.studentNumber();
        this.dormitoryType = MemberDormitoryType.fromDescription(requestDto.dormitoryType());
        this.authority = RoleType.ROLE_MEMBER;
    }

    @PrePersist
    private void init() {
        followingCount = 0;
        followerCount = 0;
    }
}
