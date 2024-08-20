package dormitoryfamily.doomz.domain.board.comment.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.comment.entity.Comment;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;

public record CommentCreatedEvent(
        Comment comment,
        Article article,
        NotificationType notificationType
) {
}
