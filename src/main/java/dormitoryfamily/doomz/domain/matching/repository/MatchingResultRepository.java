package dormitoryfamily.doomz.domain.matching.repository;

import dormitoryfamily.doomz.domain.matching.entity.MatchingResult;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingResultRepository extends JpaRepository<MatchingResult, Long> {

    @Query("SELECT m FROM MatchingResult m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingResult> findByMembers(Member loginMember, Member targetMember);
}