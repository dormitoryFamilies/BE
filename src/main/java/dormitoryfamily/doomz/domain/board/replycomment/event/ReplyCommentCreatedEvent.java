package dormitoryfamily.doomz.domain.board.replycomment.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.replycomment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;

public record ReplyCommentCreatedEvent(
        ReplyComment replyComment,
        Article article,
        NotificationType notificationType
) {
}
