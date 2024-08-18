package dormitoryfamily.doomz.domain.notification.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n " +
            "WHERE n.receiver = :receiver " +
            "ORDER BY n.isRead ASC, n.createdAt DESC")
    Page<Notification> findAllByReceiver(@Param("receiver") Member receiver, Pageable pageable);

    @Query("SELECT COUNT(n) > 0 FROM Notification n WHERE n.receiver.id = :receiverId AND n.notificationType = 'CHAT' AND n.isRead = false")
    boolean existsUnreadChatNotification(@Param("receiverId") Long receiverId);
}
