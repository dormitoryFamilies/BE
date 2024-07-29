package dormitoryfamily.doomz.domain.matchingResult.repository;

import dormitoryfamily.doomz.domain.matchingResult.entity.MatchingResult;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingResultRepository extends JpaRepository<MatchingResult, Long> {

    boolean existsBySenderAndReceiver(Member targetMember, Member loginMember);

    @Query("SELECT m FROM MatchingResult m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingResult> findByMembers(Member loginMember, Member targetMember);
}
