package dormitoryfamily.doomz.domain.notification.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.entity.Notification;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n FROM Notification n " +
            "JOIN FETCH n.sender " +
            "WHERE n.receiver = :receiver " +
            "ORDER BY n.isRead ASC, n.createdAt DESC",
            countQuery = "SELECT COUNT(n) FROM Notification n WHERE n.receiver = :receiver")
    Page<Notification> findAllByReceiver(@Param("receiver") Member receiver, Pageable pageable);

    @Query("SELECT COUNT(n) > 0 FROM Notification n " +
            "WHERE n.receiver.id = :receiverId AND n.notificationType = 'CHAT' AND n.isRead = false")
    boolean existsUnreadChatNotification(@Param("receiverId") Long receiverId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :timestamp")
    void deleteByCreatedAtBefore(@Param("timestamp") LocalDateTime timestamp);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n " +
            "WHERE n.notificationType = :notificationType AND n.targetId = :targetId AND n.receiver.id = :receiverId")
    void deleteByNotificationTypeChatAndTargetIdAndReceiverId(
            @Param("notificationType") NotificationType notificationType,
            @Param("targetId") Long targetId,
            @Param("receiverId") Long receiverId
    );


    List<Notification> findByReceiver(@Param("receiver") Member receiver);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id IN :ids")
    void updateNotificationAsRead(@Param("ids") List<Long> ids);
}
