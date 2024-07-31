package dormitoryfamily.doomz.domain.matching.repository;

import dormitoryfamily.doomz.domain.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingRequest> findByMembers(Member loginMember, Member targetMember);

    Optional<MatchingRequest> findBySenderAndReceiver(Member targetMember, Member loginMember);

    Page<MatchingRequest> findBySenderOrderByCreatedAtDesc(Member loginMember, Pageable pageable);

    Page<MatchingRequest> findByReceiverOrderByCreatedAtDesc(Member loginMember, Pageable pageable);
}
