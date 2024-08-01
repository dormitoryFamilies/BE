package dormitoryfamily.doomz.domain.roommate.matching.repository;

import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingRequest> findByMembers(Member loginMember, Member targetMember);

    Optional<MatchingRequest> findBySenderAndReceiver(Member targetMember, Member loginMember);

}
