package dormitoryfamily.doomz.domain.follow.repository;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    List<Follow> findAllByFollowerOrderByCreatedAtDesc(Member follower);

    List<Follow> findAllByFollowingOrderByCreatedAtDesc(Member following);
}
