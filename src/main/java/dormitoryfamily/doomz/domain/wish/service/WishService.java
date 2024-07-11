package dormitoryfamily.doomz.domain.wish.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.dto.response.MemberDetailsResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.WishMemberResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.domain.wish.exception.NotWishedArticleException;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;
    private final FollowRepository followRepository;

    public void saveWish(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        checkArticleIsNotWished(loginMember.getId(), articleId);
        Wish wish = Wish.createWish(loginMember, article);
        wishRepository.save(wish);
        article.increaseWishCount();
    }

    public MemberProfileListResponseDto getWishMembers(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        getArticleById(articleId);
        List<Wish> wishes = wishRepository.findAllByArticleIdOrderByCreatedAtDesc(articleId);
        List<MemberDetailsResponseDto> memberProfiles = wishes.stream()
                .map(wish -> {
                    Member wishMember = wish.getMember();
                    boolean isFollowing = followRepository.existsByFollowerAndFollowing(loginMember, wishMember);
                    return MemberDetailsResponseDto.fromEntity(wishMember, isFollowing);
                }).collect(Collectors.toList());
        return MemberProfileListResponseDto.toDto(memberProfiles);
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

        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, getSimpleArticleResponseDto(articles));
    }

    private List<Long> getArticleIds(List<Wish> wishes) {
        return wishes.stream()
                .map(wish -> wish.getArticle().getId())
                .toList();
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDto(Slice<Article> articles) {
        return articles.stream()
                .map(article -> {
                    return SimpleArticleResponseDto.fromEntity(article, true);
                })
                .toList();
    }
}
