package dormitoryfamily.doomz.domain.member.entity.type;

import lombok.Getter;

@Getter
public enum MemberDormitoryType {

    JINRIGWAN("진리관"),
    JEONGUIGWAN("정의관"),
    GAECHEOKGWAN("개척관"),
    GYEYOUNGWON("계영원"),
    JISEONGWAN("지선관"),
    MYEONGDEOKGWAN("명덕관"),
    SINMINGWAN("신민관"),
    INUIGWAN("인의관"),
    YEJIGWAN("예지관"),
    DEUNGYONGGWAN("등용관");

    private final String name;

    MemberDormitoryType(String name) {
        this.name = name;
    }
}
