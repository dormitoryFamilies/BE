package dormitoryfamily.doomz.domain.roommate.matching.repository;

import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingResultRepository extends JpaRepository<MatchingResult, Long> {

    @Query("SELECT m FROM MatchingResult m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingResult> findByMembers(Member loginMember, Member targetMember);

    @Query("SELECT m FROM MatchingResult m WHERE m.sender = :member OR m.receiver = :member")
    Optional<MatchingResult> findBySenderOrReceiver(Member member);
}
