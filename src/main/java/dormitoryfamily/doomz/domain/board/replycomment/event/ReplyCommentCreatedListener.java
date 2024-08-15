package dormitoryfamily.doomz.domain.board.replycomment.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.replycomment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyCommentCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleReplyCommentCreatedEvent(ReplyCommentCreatedEvent event) {
        ReplyComment replyComment = event.replyComment();
        Article article = event.article();

        notificationService.send(replyComment.getComment().getMember(), replyComment.getMember(), event.notificationType(), article.getTitle(), article.getId());
    }
}
