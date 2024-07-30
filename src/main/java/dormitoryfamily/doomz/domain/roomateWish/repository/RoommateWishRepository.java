package dormitoryfamily.doomz.domain.roomateWish.repository;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomateWish.entity.RoommateWish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoommateWishRepository extends JpaRepository<RoommateWish, Long> {

    Optional<RoommateWish> findByWisherAndWished(Member wisher, Member wished);

    Slice<RoommateWish> findByWisherOrderByCreatedAtDesc(Member wisher, Pageable pageable);
}
