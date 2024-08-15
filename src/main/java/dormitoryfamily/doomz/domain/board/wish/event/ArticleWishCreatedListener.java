package dormitoryfamily.doomz.domain.board.wish.event;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.wish.entity.ArticleWish;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleWishCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleArticleWishCreatedEvent(ArticleWishCreatedEvent event) {
        ArticleWish articleWish = event.articleWish();
        Article article = event.article();

        notificationService.send(article.getMember(), articleWish.getMember(), event.notificationType(), article.getTitle(), article.getId());
    }
}
