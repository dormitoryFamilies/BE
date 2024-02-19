package dormitoryfamily.doomz.domain.wish.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;

    public void saveWish(Member member, Long articleId) {
        Article article = getArticleById(articleId);
        article.increaseWishCount();
        Wish wish = createWish(article, member);
        wishRepository.save(wish);
    }

    private Article getArticleById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private Wish createWish(Article article, Member member){
        return Wish.builder().member(member).article(article).build();
    }

}
