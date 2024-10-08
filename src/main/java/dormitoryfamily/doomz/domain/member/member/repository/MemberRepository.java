package dormitoryfamily.doomz.domain.member.member.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.entity.type.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m " +
            "WHERE LOWER(m.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND m.authority = 'ROLE_VERIFIED_STUDENT' " +
            "ORDER BY m.createdAt DESC")
    Page<Member> findVerifiedMembersByKeyword(String keyword, Pageable pageable);

    @Query("SELECT m FROM Member m " +
            "WHERE m.authority = 'ROLE_VERIFIED_STUDENT' " +
            "ORDER BY m.createdAt DESC")
    Page<Member> findAllVerifiedMembers(Pageable pageable);

    boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m " +
            "WHERE m.authority = 'ROLE_MEMBER' " +
            "ORDER BY m.createdAt ASC")
    Page<Member> findNonVerifiedMember(Pageable pageable);

    /**
     * 개발용 임시. 삭제 예정
     */
    @Query("UPDATE Member m SET m.authority = :authority WHERE m.id = :memberId")
    @Modifying
    void changeMyAuthority(@Param("authority") RoleType authority, @Param("memberId") Long memberId);
}
