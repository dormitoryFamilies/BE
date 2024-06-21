package dormitoryfamily.doomz.domain.article.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleSearchRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.wish.entity.Wish;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final WishRepository wishRepository;

    public CreateArticleResponseDto save(PrincipalDetails principalDetails, ArticleRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Article article = ArticleRequestDto.toEntity(loginMember, requestDto);

        articleRepository.save(article);
        saveArticleImages(article, requestDto);

        return CreateArticleResponseDto.fromEntity(article);
    }

    private void saveArticleImages(Article article, ArticleRequestDto requestDto) {
        if (!requestDto.imagesUrls().isEmpty()) {
            requestDto.imagesUrls().forEach(url -> {
                ArticleImage articleImage = ArticleImage.builder().article(article).imageUrl(url).build();
                articleImageRepository.save(articleImage);
            });
        }
    }

    public ArticleResponseDto findArticle(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        List<ArticleImage> articleImages = articleImageRepository.findByArticleId(articleId);
        boolean isWished = checkIfArticleIsWished(article, loginMember);
        boolean isWriter = isWriter(loginMember, article.getMember());

        article.plusViewCount();
        return ArticleResponseDto.fromEntity(loginMember, article, isWished, isWriter, articleImages);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private boolean checkIfArticleIsWished(Article article, Member loginMember) {
        return wishRepository.existsByMemberIdAndArticleId(loginMember.getId(), article.getId());
    }

    public void updateArticle(PrincipalDetails principalDetails, Long articleId, ArticleRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        if (!isWriter(loginMember, article.getMember())) {
            throw new InvalidMemberAccessException();
        }

        articleImageRepository.deleteAllByArticle(article);
        saveArticleImages(article, requestDto);

        article.updateArticle(requestDto);
    }

    private boolean isWriter(Member loginMember, Member writer) {
        return Objects.equals(loginMember.getId(), writer.getId());
    }

    public void deleteArticle(PrincipalDetails principalDetails, Long articleId) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        if (!isWriter(loginMember, article.getMember())) {
            throw new InvalidMemberAccessException();
        }

        articleRepository.delete(article);
    }

    public void changeStatus(PrincipalDetails principalDetails, Long articleId, String status) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId);
        if (!isWriter(loginMember, article.getMember())) {
            throw new InvalidMemberAccessException();
        }

        StatusType statusType = StatusType.fromDescription(status);
        article.changeStatus(statusType);
    }

    public ArticleListResponseDto findAllArticles(PrincipalDetails principalDetails,
                                                  String articleDormitoryType,
                                                  ArticleRequest request,
                                                  Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        Slice<Article> articles = articleRepository
                .findAllByDormitoryTypeAndBoardType(dormitoryType, null, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtos(Member loginMember, Slice<Article> articles) {
        List<Wish> memberWishes = wishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWishedByList(article, loginMember, memberWishes);
                    return SimpleArticleResponseDto.fromEntity(article, isWished);
                })
                .toList();
    }

    private boolean checkIfArticleIsWishedByList(Article article, Member loginMember, List<Wish> memberWishes) {
        return memberWishes.stream()
                .anyMatch(wish -> wish.getArticle().getId().equals(article.getId()));
    }

    public ArticleListResponseDto findAllArticles(PrincipalDetails principalDetails,
                                                  String articleDormitoryType,
                                                  String articleBoardType,
                                                  ArticleRequest request,
                                                  Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);
        BoardType boardType = BoardType.fromDescription(articleBoardType);

        Slice<Article> articles = articleRepository
                .findAllByDormitoryTypeAndBoardType(dormitoryType, boardType, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    public ArticleListResponseDto findMyArticles(PrincipalDetails principalDetails,
                                                 String articleDormitoryType,
                                                 String articleBoardType,
                                                 ArticleRequest request,
                                                 Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        BoardType boardType;
        if (articleBoardType.equals("all")) {
            boardType = null;
        } else {
            boardType = BoardType.fromDescription(articleBoardType);
        }

        Slice<Article> articles = articleRepository
                .findMyArticleByDormitoryTypeAndBoardType(loginMember, dormitoryType, boardType, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtosWithMember(loginMember, articles));
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtosWithMember(Member loginMember, Slice<Article> articles) {
        List<Wish> memberWishes = wishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWishedByList(article, loginMember, memberWishes);
                    return SimpleArticleResponseDto.fromEntityWithMember(article, loginMember, isWished);
                })
                .toList();
    }

    public ArticleListResponseDto searchArticles(PrincipalDetails principalDetails,
                                                 String articleDormitoryType,
                                                 ArticleSearchRequestDto requestDto,
                                                 Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        Slice<Article> articles = articleRepository.searchArticles(dormitoryType, requestDto.q(), pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }
}
