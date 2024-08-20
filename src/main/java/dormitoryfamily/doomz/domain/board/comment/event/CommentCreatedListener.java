package dormitoryfamily.doomz.domain.board.comment.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.comment.entity.Comment;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {
        Comment comment = event.comment();
        Article article = event.article();

        notificationService.send(article.getMember(), comment.getMember(), event.notificationType(), article.getTitle(), article.getId());
    }
}
