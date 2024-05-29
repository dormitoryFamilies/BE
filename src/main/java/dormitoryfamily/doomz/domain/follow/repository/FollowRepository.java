package dormitoryfamily.doomz.domain.follow.repository;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    Page<Follow> findAllByFollowerOrderByCreatedAtDesc(Member follower, Pageable pageable);

    Page<Follow> findAllByFollowingOrderByCreatedAtDesc(Member following, Pageable pageable);
}
