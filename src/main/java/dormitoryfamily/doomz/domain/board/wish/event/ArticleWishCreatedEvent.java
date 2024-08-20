package dormitoryfamily.doomz.domain.board.wish.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.wish.entity.ArticleWish;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;

public record ArticleWishCreatedEvent(
        ArticleWish articleWish,
        Article article,
        NotificationType notificationType
) {
}
