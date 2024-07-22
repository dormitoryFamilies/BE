package dormitoryfamily.doomz.domain.member.entity.type;

public enum RoleType {

    ROLE_VISITOR, // 회원가입 이전 유저
    ROLE_MEMBER, // 프로필 설정 완료 유저
    ROLE_REJECTED_MEMBER, // 학생증 인증 거절된 유저
    ROLE_VERIFIED_STUDENT, // 학생증 인증까지 완료한 유저
    ROLE_ADMIN; // 관리자

    private RoleType() {
    }
}
