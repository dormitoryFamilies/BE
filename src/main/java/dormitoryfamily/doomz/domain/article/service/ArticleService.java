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
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
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

    public CreateArticleResponseDto save(Member loginMember, ArticleRequestDto requestDto) {
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

    public ArticleResponseDto findArticle(Member loginMember, Long articleId) {
        Article article = getArticleById(articleId);
        List<ArticleImage> articleImages = articleImageRepository.findByArticleId(articleId);
        boolean isWished = checkIfArticleIsWished(article, loginMember);

        article.plusViewCount();
        return ArticleResponseDto.fromEntity(loginMember, article, isWished, articleImages);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    private boolean checkIfArticleIsWished(Article article, Member loginMember) {
        return wishRepository.existsByMemberIdAndArticleId(loginMember.getId(), article.getId());
    }

    public void updateArticle(Member loginMember, Long articleId, ArticleRequestDto requestDto) {
        Article article = getArticleById(articleId);
        isWriter(loginMember, article.getMember());

        articleImageRepository.deleteAllByArticle(article);
        saveArticleImages(article, requestDto);

        article.updateArticle(requestDto);
    }

    private void isWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    public void deleteArticle(Member loginMember, Long articleId) {
        Article article = getArticleById(articleId);
        isWriter(loginMember, article.getMember());

        articleRepository.delete(article);
    }

    public void changeStatus(Member loginMember, Long articleId, String status) {
        Article article = getArticleById(articleId);
        isWriter(loginMember, article.getMember());

        StatusType statusType = StatusType.fromDescription(status);
        article.changeStatus(statusType);
    }

    public ArticleListResponseDto findAllArticles(
            Member loginMember,
            String articleDormitoryType,
            ArticleRequest request,
            Pageable pageable
    ) {
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        Slice<Article> articles = articleRepository
                .findAllByDormitoryTypeAndBoardType(dormitoryType, null, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtos(Member loginMember, Slice<Article> articles) {
        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWished(article, loginMember);
                    return SimpleArticleResponseDto.fromEntity(article, isWished);
                })
                .toList();
    }

    public ArticleListResponseDto findAllArticles(
            Member loginMember,
            String articleDormitoryType,
            String articleBoardType,
            ArticleRequest request,
            Pageable pageable
    ) {
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);
        BoardType boardType = BoardType.fromDescription(articleBoardType);

        Slice<Article> articles = articleRepository
                .findAllByDormitoryTypeAndBoardType(dormitoryType, boardType, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    public ArticleListResponseDto findMyArticles(Member loginMember,
                                                 String articleDormitoryType,
                                                 String articleBoardType,
                                                 Pageable pageable
    ) {
       ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        BoardType boardType = null;
        if(articleBoardType!=null){
            boardType = BoardType.fromDescription(articleBoardType);
        }

        Slice<Article> articles = articleRepository
                .findMyArticleByDormitoryTypeAndBoardType(loginMember,dormitoryType, boardType, pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    public ArticleListResponseDto searchArticles(Member loginMember,
                                                 String articleDormitoryType,
                                                 ArticleSearchRequestDto requestDto,
                                                 Pageable pageable
    ) {
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        Slice<Article> articles = articleRepository.searchArticles(dormitoryType, requestDto.q(), pageable);
        return ArticleListResponseDto.fromResponseDtos(articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

}
