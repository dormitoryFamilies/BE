package dormitoryfamily.doomz.domain.articleWish.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.articleWish.entity.ArticleWish;
import dormitoryfamily.doomz.domain.articleWish.exception.CannotWishYourArticleException;
import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.dto.response.MemberDetailsResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.articleWish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.domain.articleWish.exception.NotWishedArticleException;
import dormitoryfamily.doomz.domain.articleWish.repository.ArticleWishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleWishService {

    private final ArticleRepository articleRepository;
    private final ArticleWishRepository articleWishRepository;
    private final FollowRepository followRepository;

    public void saveArticleWish(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);

        validateArticleWishRequest(loginMember, article);

        ArticleWish articleWish = ArticleWish.createArticleWish(loginMember, article);
        articleWishRepository.save(articleWish);

        article.increaseWishCount();
    }

    private void validateArticleWishRequest(Member loginMember, Article article){
        checkArticleIfNotWished(loginMember.getId(), article.getId());
        checkIfNotArticleWriter(loginMember.getId(), article);
    }

    private void checkArticleIfNotWished(Long memberId, Long articleId) {
        if (articleWishRepository.existsByMemberIdAndArticleId(memberId, articleId)) {
            throw new AlreadyWishedArticleException();
        }
    }

    private void checkIfNotArticleWriter(Long memberId, Article article){
        if(Objects.equals(article.getMember().getId(), memberId)){
            throw new CannotWishYourArticleException();
        }
    }

    public MemberProfileListResponseDto findArticleWishMembers(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);

        validateArticleWriter(article, loginMember);

        List<MemberDetailsResponseDto> memberProfiles = articleWishRepository.findAllByArticleIdOrderByCreatedAtDesc(articleId)
                .stream()
                .map(wish -> createMemberDetailsResponseDto(loginMember, wish.getMember()))
                .collect(Collectors.toList());

        return MemberProfileListResponseDto.from(memberProfiles);
    }

    private void validateArticleWriter(Article article, Member loginMember) {
        if (!Objects.equals(article.getMember().getId(), loginMember.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    private MemberDetailsResponseDto createMemberDetailsResponseDto(Member loginMember, Member wishMember) {
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(loginMember, wishMember);
        return MemberDetailsResponseDto.fromEntity(wishMember, isFollowing);
    }

    public void removeArticleWish(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);

        ArticleWish articleWish = getWishByMemberIdAndArticleId(loginMember.getId(), articleId);
        articleWishRepository.delete(articleWish);

        article.decreaseWishCount();
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private ArticleWish getWishByMemberIdAndArticleId(Long memberId, Long articleId) {
        return articleWishRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(NotWishedArticleException::new);
    }

    public ArticleListResponseDto findMyArticleWishes(PrincipalDetails principalDetails, String articleDormitoryType, ArticleRequest request, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        List<ArticleWish> myWishes = articleWishRepository.findAllByMemberId(loginMember.getId());
        List<Long> articleIds = getArticleIds(myWishes);

        Slice<Article> articles = articleRepository
                .findAllByIdInAndDormitoryTypeAndBoardType(articleIds, dormitoryType, null, request, pageable);

        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, CreateSimpleArticleResponseDto(articles));
    }

    private List<Long> getArticleIds(List<ArticleWish> wishes) {
        return wishes.stream()
                .map(wish -> wish.getArticle().getId())
                .toList();
    }

    private List<SimpleArticleResponseDto> CreateSimpleArticleResponseDto(Slice<Article> articles) {

        return articles.stream()
                .map(article -> {
                    return SimpleArticleResponseDto.fromEntity(article, true);
                })
                .toList();
    }
}
