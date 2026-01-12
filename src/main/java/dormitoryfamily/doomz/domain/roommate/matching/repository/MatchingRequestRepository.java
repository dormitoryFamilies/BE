package dormitoryfamily.doomz.domain.roommate.matching.repository;

import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.roommate.matching.entity.RequestStatus;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver = :targetMember) " +
            "OR (m.sender = :targetMember AND m.receiver = :loginMember)")
    Optional<MatchingRequest> findByMembers(Member loginMember, Member targetMember);

    Optional<MatchingRequest> findBySenderAndReceiver(Member sender, Member receiver);

    Page<MatchingRequest> findBySenderOrderByCreatedAtDesc(Member sender, Pageable pageable);

    Page<MatchingRequest> findByReceiverOrderByCreatedAtDesc(Member receiver, Pageable pageable);

    long countMatchingRequestsByReceiver(Member receiver);

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver.id IN :candidateIds) " +
            "OR (m.receiver = :loginMember AND m.sender.id IN :candidateIds)")
    List<MatchingRequest> findAllByMemberAndCandidateIds(
            @Param("loginMember") Member loginMember,
            @Param("candidateIds") List<Long> candidateIds);

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :loginMember AND m.receiver IN :candidates) " +
            "OR (m.receiver = :loginMember AND m.sender IN :candidates)")
    List<MatchingRequest> findAllByMemberAndCandidates(
            @Param("loginMember") Member loginMember,
            @Param("candidates") List<Member> candidates);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE MatchingRequest m
           SET m.status = :newStatus
         WHERE m.id = :id
           AND m.status = :currentStatus
    """)
    int updateStatus(
            @Param("id") Long id,
            @Param("currentStatus") RequestStatus currentStatus,
            @Param("newStatus") RequestStatus newStatus
    );

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE m.sender = :sender AND m.receiver = :receiver " +
            "AND m.status = :status")
    Optional<MatchingRequest> findBySenderAndReceiverAndStatus(
            @Param("sender") Member sender,
            @Param("receiver") Member receiver,
            @Param("status") RequestStatus status
    );

    @Query("SELECT m FROM MatchingRequest m " +
            "WHERE (m.sender = :member OR m.receiver = :member) " +
            "AND m.status = dormitoryfamily.doomz.domain.roommate.matching.entity.RequestStatus.ACCEPTED")
    Optional<MatchingRequest> findBySenderOrReceiverAndStatus(@Param("member") Member member);

    @Modifying(clearAutomatically = true)
    @Query("""
        DELETE FROM MatchingRequest m
         WHERE ((m.sender = :loginMember AND m.receiver = :targetMember)
            OR (m.sender = :targetMember AND m.receiver = :loginMember))
           AND m.status = :status
    """)
    int deleteByMembersAndStatus(
            @Param("loginMember") Member loginMember,
            @Param("targetMember") Member targetMember,
            @Param("status") RequestStatus status
    );
}
