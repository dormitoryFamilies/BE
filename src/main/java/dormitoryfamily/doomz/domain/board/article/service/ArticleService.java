package dormitoryfamily.doomz.domain.board.article.service;

import dormitoryfamily.doomz.domain.board.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.board.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.board.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.board.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.SimpleArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.board.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.board.wish.entity.ArticleWish;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.board.wish.repository.ArticleWishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleWishRepository articleWishRepository;

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
        Article article = getArticleById(articleId, true);
        boolean isWished = checkIfArticleIsWished(article, loginMember);
        boolean isWriter = isWriter(loginMember, article.getMember());

        article.plusViewCount();
        return ArticleResponseDto.fromEntity(loginMember, article, isWished, isWriter, article.getArticleImages());
    }

    private Article getArticleById(Long articleId, boolean fetchJoinRequired) {
        if (fetchJoinRequired) {
            return articleRepository.findById(articleId)
                    .orElseThrow(ArticleNotExistsException::new);
        } else {
            return articleRepository.findByIdWithoutFetch(articleId)
                    .orElseThrow(ArticleNotExistsException::new);
        }
    }

    private boolean checkIfArticleIsWished(Article article, Member loginMember) {
        return articleWishRepository.existsByMemberIdAndArticleId(loginMember.getId(), article.getId());
    }

    public void updateArticle(PrincipalDetails principalDetails, Long articleId, ArticleRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId, false);
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
        Article article = getArticleById(articleId, false);
        if (!isWriter(loginMember, article.getMember())) {
            throw new InvalidMemberAccessException();
        }

        articleRepository.delete(article);
    }

    public void changeStatus(PrincipalDetails principalDetails, Long articleId, String status) {
        Member loginMember = principalDetails.getMember();
        Article article = getArticleById(articleId, false);
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
        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtos(Member loginMember, Slice<Article> articles) {
        List<ArticleWish> memberWishes = articleWishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWishedByList(article, memberWishes);
                    return SimpleArticleResponseDto.fromEntity(article, isWished);
                })
                .toList();
    }

    private boolean checkIfArticleIsWishedByList(Article article, List<ArticleWish> memberWishes) {
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
        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, getSimpleArticleResponseDtos(loginMember, articles));
    }

    public ArticleListResponseDto findMyArticles(PrincipalDetails principalDetails,
                                                 String articleDormitoryType,
                                                 String articleBoardType,
                                                 ArticleRequest request,
                                                 Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        BoardType boardType = BoardType.fromDescription(articleBoardType);
        if (boardType.equals(BoardType.ALL)) {
            boardType = null;
        }

        Slice<Article> articles = articleRepository
                .findMyArticleByDormitoryTypeAndBoardType(loginMember, dormitoryType, boardType, request, pageable);
        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, getSimpleArticleResponseDtosWithMember(loginMember, articles));
    }

    private List<SimpleArticleResponseDto> getSimpleArticleResponseDtosWithMember(Member loginMember, Slice<Article> articles) {
        List<ArticleWish> memberWishes = articleWishRepository.findAllByMemberId(loginMember.getId());

        return articles.stream()
                .map(article -> {
                    boolean isWished = checkIfArticleIsWishedByList(article, memberWishes);
                    return SimpleArticleResponseDto.fromEntityWithMember(article, loginMember, isWished);
                })
                .toList();
    }

    public ArticleListResponseDto searchArticles(PrincipalDetails principalDetails,
                                                 String articleDormitoryType,
                                                 SearchRequestDto requestDto,
                                                 Pageable pageable
    ) {
        Member loginMember = principalDetails.getMember();
        ArticleDormitoryType dormitoryType = ArticleDormitoryType.fromName(articleDormitoryType);

        Slice<Article> articles = articleRepository.searchArticles(dormitoryType, requestDto.q(), pageable);
        return ArticleListResponseDto.fromResponseDtos(loginMember, articles, getSimpleArticleResponseDtos(loginMember, articles));
    }
}
