package dormitoryfamily.doomz.domain.chatroom.repository;

import dormitoryfamily.doomz.domain.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.initiator = :member1 AND cr.participant = :member2) " +
            "OR (cr.initiator = :member2 AND cr.participant = :member1)")
    Optional<ChatRoom> findByInitiatorAndParticipant(Member member1, Member member2);

    Optional<ChatRoom> findByRoomUUID(String roomUUID);

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.initiator = :member AND cr.initiatorEnteredAt IS NOT NULL) OR " +
            "(cr.participant = :member AND cr.participantEnteredAt IS NOT NULL)")
    Slice<ChatRoom> findAllByMember(Member member, Pageable pageable);

    @Query("SELECT COALESCE(SUM(CASE WHEN cr.participant = :member AND cr.participantEnteredAt IS NOT NULL THEN cr.participantUnreadCount ELSE 0 END), 0) + " +
            "COALESCE(SUM(CASE WHEN cr.initiator = :member AND cr.participantEnteredAt IS NOT NULL THEN cr.initiatorUnreadCount ELSE 0 END), 0) " +
            "FROM ChatRoom cr")
    Integer findTotalUnreadCountForMember(Member member);

    @Query("SELECT c FROM ChatRoom c " +
            "WHERE ((c.initiator = :member AND c.initiatorEnteredAt IS NOT NULL AND LOWER(c.participant.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR (c.participant = :member AND c.participantEnteredAt IS NOT NULL AND LOWER(c.initiator.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Slice<ChatRoom> findByMemberAndNickname(Member member, String keyword, Pageable pageable);
}

