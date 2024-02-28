package dormitoryfamily.doomz.domain.follow.repository;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
