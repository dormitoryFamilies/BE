package dormitoryfamily.doomz.domain.wish.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.dto.WishMemberListResponseDto;
import dormitoryfamily.doomz.domain.wish.dto.WishMemberResponseDto;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.domain.wish.exception.NotWishedArticleException;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;

    public void saveWish(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        checkArticleIsNotWished(loginMember.getId(), articleId);
        Wish wish = Wish.createWish(loginMember, article);
        wishRepository.save(wish);
        article.increaseWishCount();
    }

    public WishMemberListResponseDto getWishMembers(Long articleId) {
        getArticleById(articleId);
        List<Wish> wishes = wishRepository.findAllByArticleIdOrderByCreatedAtDesc(articleId);
        List<WishMemberResponseDto> wishMemberResponseDtos = wishes.stream()
                .map(wish -> WishMemberResponseDto.fromMember(wish.getMember())).collect(toList());
        return WishMemberListResponseDto.toDto(wishMemberResponseDtos);
    }

    public void removeWish(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        Wish wish = getWishByMemberIdAndArticleId(loginMember.getId(), articleId);
        wishRepository.delete(wish);
        article.decreaseWishCount();
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private void checkArticleIsNotWished(Long memberId, Long articleId) {
        if (wishRepository.existsByMemberIdAndArticleId(memberId, articleId)) {
            throw new AlreadyWishedArticleException();
        }
    }

    private Wish getWishByMemberIdAndArticleId(Long memberId, Long articleId) {
        return wishRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(NotWishedArticleException::new);
    }

    public ArticleListResponseDto findMyArticleWishes(PrincipalDetails principalDetails, String articleDormitoryType, ArticleRequest request, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        List<Wish> myWishes = wishRepository.findAllByMemberId(loginMember.getId());
        List<Long> articleIds = getArticleIds(myWishes);

        Slice<Article> articles = articleRepository
                .findAllByIdInAndDormitoryTypeAndBoardType(articleIds, dormitoryType, null, request, pageable);

        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDto(articles));
    }

    private List<Long> getArticleIds(List<Wish> wishes) {
        return wishes.stream()
                .map(wish -> wish.getArticle().getId())
                .toList();
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDto(Slice<Article> articles){
        return  articles.stream()
                .map(article -> {
                    return SimpleArticleResponseDto.fromEntity(article, true);
                })
                .toList();
    }
}
