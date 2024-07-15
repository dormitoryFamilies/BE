package dormitoryfamily.doomz.domain.member.repository;

import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m " +
            "WHERE LOWER(m.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND m.id NOT IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId) " +
            "AND m.authority = 'ROLE_VERIFIED_STUDENT' " +
            "ORDER BY m.createdAt DESC")
    List<Member> findMembersExcludingFollowed(Long followerId, String keyword);

    @Query("SELECT m FROM Member m " +
            "WHERE m.id NOT IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId) " +
            "AND m.authority = 'ROLE_VERIFIED_STUDENT' "+
            "ORDER BY m.createdAt DESC")
    List<Member> findAllMembersExcludingFollowed(Long followerId);

    boolean existsByNickname(String nickname);
}
