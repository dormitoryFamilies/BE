package dormitoryfamily.doomz.domain.chat.repository;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findTopByRoomUUIDOrderByCreatedAtDesc(String roomUUID);

    List<Chat> findAllByRoomUUID(String roomUUID);

    @Transactional
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.id <= :lastChatId")
    void deleteChatsLessThanChatId(Long lastChatId);
}
