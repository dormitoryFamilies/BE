package dormitoryfamily.doomz.domain.follow.repository;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    Page<Follow> findAllByFollowerOrderByCreatedAtDesc(Member follower, Pageable pageable);

    Page<Follow> findAllByFollowingOrderByCreatedAtDesc(Member following, Pageable pageable);

    @Query("SELECT f FROM Follow f WHERE f.follower = :follower AND LOWER(f.following.nickname) " +
            "LIKE LOWER(concat('%', :keyword, '%')) ORDER BY f.createdAt DESC")
    List<Follow> findByFollowerAndFollowingNicknameContaining(Member follower, String keyword);

    @Query("SELECT f FROM Follow f WHERE f.following = :following AND LOWER(f.follower.nickname) " +
            "LIKE LOWER(concat('%', :keyword, '%')) ORDER BY f.createdAt DESC")
    List<Follow> findByFollowingAndFollowerNicknameContaining(Member following, String keyword);
}
