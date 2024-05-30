package dormitoryfamily.doomz.domain.chatRoom.repository;

import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.sender = :member1 AND cr.receiver = :member2) " +
            "OR (cr.sender = :member2 AND cr.receiver = :member1)")
    Optional<ChatRoom> findBySenderAndReceiver(Member member1, Member member2);

    Optional<ChatRoom> findByRoomUUID(String roomUUID);

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.sender = :member AND cr.senderEnteredAt IS NOT NULL) OR " +
            "(cr.receiver = :member AND cr.receiverEnteredAt IS NOT NULL)")
    Slice<ChatRoom> findAllByMember(Member member, Pageable pageable);

    @Query("SELECT COALESCE(SUM(CASE WHEN cr.receiver = :member AND cr.receiverEnteredAt IS NOT NULL THEN cr.receiverUnreadCount ELSE 0 END), 0) + " +
            "COALESCE(SUM(CASE WHEN cr.sender = :member AND cr.receiverEnteredAt IS NOT NULL THEN cr.senderUnreadCount ELSE 0 END), 0) " +
            "FROM ChatRoom cr")
    Integer findTotalUnreadCountForMember(Member member);

    @Query("SELECT c FROM ChatRoom c " +
            "WHERE ((c.sender = :member AND c.senderEnteredAt IS NOT NULL AND LOWER(c.receiver.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR (c.receiver = :member AND c.receiverEnteredAt IS NOT NULL AND LOWER(c.sender.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Slice<ChatRoom> findChatRoomsByKeyword(Member member, String keyword, Pageable pageable);
}

