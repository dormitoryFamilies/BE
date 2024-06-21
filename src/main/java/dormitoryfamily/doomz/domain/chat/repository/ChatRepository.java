package dormitoryfamily.doomz.domain.chat.repository;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(String roomUUID);

    Slice<Chat> findAllByChatRoomRoomUUID(String roomUUID, Pageable pageable);

    @Query("SELECT c FROM Chat c WHERE c.id < :lastChatId AND c.chatRoom.roomUUID = :roomUUID ORDER BY c.createdAt DESC")
    Slice<Chat> findAllByChatRoomRoomUUID(String roomUUID, Long lastChatId, Pageable pageable);

    List<Chat> findAllByChatRoomRoomUUID(String roomUUID);

    @Transactional
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.id <= :lastChatId")
    void deleteChatsLessThanChatId(Long lastChatId);

    int countByChatRoomRoomUUID(String roomUUID);
}
