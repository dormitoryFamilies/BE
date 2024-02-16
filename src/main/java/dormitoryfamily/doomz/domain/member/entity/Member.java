package dormitoryfamily.doomz.domain.member.entity;

import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private MemberDormitoryType dormitoryType;
}
