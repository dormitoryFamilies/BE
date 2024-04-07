package dormitoryfamily.doomz.domain.chat.repository;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.entity.type.VisibleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Chat c SET c.visible = :visibleStatus WHERE c IN :chatsToUpdate")
    void bulkUpdateChatVisibility(@Param("chatsToUpdate") List<Chat> chatsToUpdateToSomething, @Param("visibleStatus") VisibleStatus visibleStatus);

    @Modifying
    @Query("DELETE FROM Chat c WHERE c IN :chatsToDelete")
    void bulkDeleteChats(@Param("chatsToDelete") List<Chat> chatsToDelete);
}
