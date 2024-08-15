package dormitoryfamily.doomz.domain.notification.repository;

import dormitoryfamily.doomz.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
