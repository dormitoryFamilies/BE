package dormitoryfamily.doomz.domain.chatting.chat.repository;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;


public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Optional<Chat> findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(String roomUUID);

    List<Chat> findAllByChatRoomRoomUUID(String roomUUID);

    @Transactional
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.chatRoom.roomUUID = :roomUUID AND c.createdAt < :enteredAt")
    void deleteByCreatedAtBefore(String roomUUID, LocalDateTime enteredAt);

    boolean existsByChatRoomRoomUUID(String roomUUID);
}
