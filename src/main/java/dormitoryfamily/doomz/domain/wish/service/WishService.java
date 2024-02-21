package dormitoryfamily.doomz.domain.wish.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.dto.WishListResponseDto;
import dormitoryfamily.doomz.domain.wish.dto.WishResponseDto;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.domain.wish.exception.NotWishedArticleException;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;

    public void saveWish(Member member, Long articleId) {
        Article article = getArticleById(articleId);
        checkArticleIsNotWished(member.getId(),articleId);
        Wish wish = createWish(article, member);
        wishRepository.save(wish);
        article.increaseWishCount();
    }

    public WishListResponseDto getWishes(Long articleId){
        getArticleById(articleId);
        List<Wish> wishes = wishRepository.findByArticleId(articleId);
        List<WishResponseDto> wishResponseDto = wishes.stream()
                .map(wish -> WishResponseDto.fromEntity(wish.getMember())).collect(toList());
        return WishListResponseDto.toDto(wishResponseDto);
    }

    public void removeWish(Member member, Long articleId){
        Article article = getArticleById(articleId);
        Wish wish = getWishByMemberIdAndArticleId(member.getId(), articleId);
        wishRepository.delete(wish);
        article.decreaseWishCount();
    }

    private Article getArticleById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private void checkArticleIsNotWished(Long memberId, Long articleId){
        if(wishRepository.existsByMemberIdAndArticleId(memberId, articleId)){
            throw new AlreadyWishedArticleException();
        }
    }

    private Wish createWish(Article article, Member member){
        return Wish.builder().member(member).article(article).build();
    }

    private Wish getWishByMemberIdAndArticleId(Long memberId, Long articleId){
        return wishRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(NotWishedArticleException::new);
    }

}
