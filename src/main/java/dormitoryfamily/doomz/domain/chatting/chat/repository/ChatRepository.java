package dormitoryfamily.doomz.domain.chatting.chat.repository;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Optional<Chat> findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(String roomUUID);

    List<Chat> findAllByChatRoomRoomUUID(String roomUUID);

    @Transactional
    @Modifying
    void deleteByCreatedAtBefore(LocalDateTime enteredAt);

    boolean existsByChatRoomRoomUUID(String roomUUID);
}
