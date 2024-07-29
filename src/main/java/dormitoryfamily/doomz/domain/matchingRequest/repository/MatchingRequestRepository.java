package dormitoryfamily.doomz.domain.matchingRequest.repository;

import dormitoryfamily.doomz.domain.matchingRequest.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

    @Query("SELECT COUNT(m) > 0 FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    boolean existsByMembers(Member loginMember, Member targetMember);

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingRequest> findByMembers(Member loginMember, Member targetMember);

    Optional<MatchingRequest> findBySenderAndReceiver(Member targetMember, Member loginMember);

}
