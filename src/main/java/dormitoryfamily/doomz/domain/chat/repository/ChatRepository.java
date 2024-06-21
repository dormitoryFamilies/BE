package dormitoryfamily.doomz.domain.chat.repository;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(String roomUUID);

    @Query("SELECT c FROM Chat c WHERE c.createdAt > :enteredAt AND c.chatRoom.roomUUID = :roomUUID ORDER BY c.createdAt DESC")
    Slice<Chat> findByRoomUUIDAndCreatedAtAfter(String roomUUID, LocalDateTime enteredAt, Pageable pageable);

    List<Chat> findAllByChatRoomRoomUUID(String roomUUID);

    @Transactional
    @Modifying
    void deleteByCreatedAtBefore(LocalDateTime enteredAt);

    @Query("SELECT c FROM Chat c " +
            "JOIN c.chatRoom cr " +
            "WHERE LOWER(c.message) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND ((cr.sender = :member AND cr.senderEnteredAt IS NOT NULL) " +
            "OR (cr.receiver = :member AND cr.receiverEnteredAt IS NOT NULL)) " +
            "ORDER BY c.createdAt DESC")
    Slice<Chat> findByChatMessage(Member member, String keyword, Pageable pageable);
}
