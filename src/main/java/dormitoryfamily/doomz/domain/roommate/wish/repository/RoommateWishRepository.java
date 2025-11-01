package dormitoryfamily.doomz.domain.roommate.wish.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.wish.entity.RoommateWish;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface RoommateWishRepository extends JpaRepository<RoommateWish, Long> {

    Optional<RoommateWish> findByWisherAndWished(Member wisher, Member wished);

    Slice<RoommateWish> findByWisherOrderByCreatedAtDesc(Member wisher, Pageable pageable);

    boolean existsByWisherAndWished(Member wisher, Member wished);

    @Query("SELECT w FROM RoommateWish w WHERE w.wisher = :wisher AND w.wished IN :wisheds")
    List<RoommateWish> findAllByWisherAndWishedIn(Member wisher, List<Member> wisheds);
}
