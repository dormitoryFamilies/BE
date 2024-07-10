package dormitoryfamily.doomz.domain.article.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.exception.InvalidArticleDormitoryTypeException;
import dormitoryfamily.doomz.domain.article.exception.InvalidBoardTypeException;
import dormitoryfamily.doomz.domain.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.repository.WishRepository;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dormitoryfamily.doomz.TestDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    ArticleImageRepository articleImageRepository;

    @Mock
    WishRepository wishRepository;

    @InjectMocks
    private ArticleService articleService;

    private PrincipalDetails principalDetails;
    private Map<String, Member> members;
    private List<Article> articles;
    private List<ArticleImage> articleImages;

    @BeforeEach
    public void setUp() {
        members = createDummyMembers();
        articles = createDummyArticles(members);
        articleImages = createDummyArticleImages(articles);

        // 로그인한 사용자 설정 = 홍길동
        principalDetails = new PrincipalDetails(members.get("홍길동"));
    }

    @Nested
    @DisplayName("사용자가 게시글을 등록할 때,")
    class registerArticle {

        @Test
        @DisplayName("정상적으로 등록할 수 있다.")
        void registerArticleSuccess() {
            //given
            ArticleRequestDto articleRequestDto = new ArticleRequestDto(
                    "양진재",
                    "도와주세요",
                    "테스트용 제목입니다.",
                    "테스트용 게시글 내용입니다.",
                    "#바퀴벌래#무서워",
                    List.of("S3/images/abcdefg/123"));

            when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> {
                Article savedArticle = invocation.getArgument(0);
                ReflectionTestUtils.setField(savedArticle, "id", 1L); // 가짜 id 세팅
                return savedArticle;
            });

            //when
            CreateArticleResponseDto responseDto = articleService.save(principalDetails, articleRequestDto);

            //then
            assertNotNull(responseDto);
            assertThat(responseDto.articleId()).isEqualTo(1L);

            verify(articleRepository, times(1)).save(any(Article.class));
            verify(articleImageRepository, times(1)).save(any(ArticleImage.class));
        }

        @Test
        @DisplayName("유효하지 않은 기숙사 유형일 시 InvalidArticleDormitoryTypeException 예외가 발생한다.")
        void InvalidArticleDormitoryTypeExceptionFail() {
            //given
            ArticleRequestDto articleRequestDto = new ArticleRequestDto(
                    "가짜재",
                    "도와주세요",
                    "테스트용 제목입니다.",
                    "테스트용 게시글 내용입니다.",
                    "#바퀴벌래#무서워",
                    List.of("S3/images/abcdefg/123"));

            //when
            //then
            assertThrows(InvalidArticleDormitoryTypeException.class, () ->
                    articleService.save(principalDetails, articleRequestDto));
        }

        @Test
        @DisplayName("유효하지 않은 게시판 유형일 시 InvalidBoardTypeException 예외가 발생한다.")
        void InvalidBoardTypeExceptionFail() {
            //given
            ArticleRequestDto articleRequestDto = new ArticleRequestDto(
                    "양진재",
                    "실패합니다",
                    "테스트용 제목입니다.",
                    "테스트용 게시글 내용입니다.",
                    "#바퀴벌래#무서워",
                    List.of("S3/images/abcdefg/123"));

            //when
            //then
            assertThrows(InvalidBoardTypeException.class, () ->
                    articleService.save(principalDetails, articleRequestDto));
        }
    }

    @Nested
    @DisplayName("게시글을 단 건 조회할 때,")
    class findOneArticle {

        @Test
        @DisplayName("모든 내용이 정상적으로 조회된다.")
        void findArticleByIdSuccess() {
            //given
            Long articleId = 1L;
            Article article = articles.get(0);

            List<String> articleImageUrls = articleImages.stream()
                    .map(ArticleImage::getImageUrl)
                    .toList();

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(articleImageRepository.findByArticleId(articleId)).thenReturn(articleImages);
            when(wishRepository.existsByMemberIdAndArticleId(principalDetails.getMember().getId(), articleId)).thenReturn(false);

            //when
            ArticleResponseDto responseDto = articleService.findArticle(principalDetails, articleId);

            //then
            assertThat(responseDto.articleId()).isEqualTo(article.getId());
            assertThat(responseDto.loginMemberId()).isEqualTo(principalDetails.getMember().getId());
            assertThat(responseDto.memberId()).isEqualTo(article.getMember().getId());
            assertThat(responseDto.profileUrl()).isEqualTo(article.getMember().getProfileUrl());
            assertThat(responseDto.title()).isEqualTo(article.getTitle());
            assertThat(responseDto.content()).isEqualTo(article.getContent());
            assertThat(responseDto.tags()).isEqualTo(article.getTags());
            assertThat(responseDto.imagesUrls()).isEqualTo(articleImageUrls);
        }

        @Test
        @DisplayName("로그인 사용자의 게시글을 조회하면 isWriter가 true 반환된다.")
        void findArticleWrittenByLoginMemberSuccess() {
            //given
            Long articleId = 1L; // 로그인한 사용자가 작성한 게시글 id
            Article article = articles.get(0);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(articleImageRepository.findByArticleId(articleId)).thenReturn(articleImages);
            when(wishRepository.existsByMemberIdAndArticleId(principalDetails.getMember().getId(), articleId)).thenReturn(false);

            //when
            ArticleResponseDto responseDto = articleService.findArticle(principalDetails, articleId);

            //then
            assertThat(responseDto.isWriter()).isTrue();
        }

        @Test
        @DisplayName("다른 사용자가 작성한 게시글을 조회 시 isWriter가 false 반환된다.")
        void findArticleWrittenByOtherMemberSuccess() {
            //given
            Long articleId = 2L; // 로그인한 사용자가 작성하지 않은 게시글 id
            Article article = articles.get(1);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(articleImageRepository.findByArticleId(articleId)).thenReturn(articleImages);
            when(wishRepository.existsByMemberIdAndArticleId(principalDetails.getMember().getId(), articleId)).thenReturn(false);

            //when
            ArticleResponseDto responseDto = articleService.findArticle(principalDetails, articleId);

            //then
            assertThat(responseDto.isWriter()).isFalse();
        }

        @Test
        @DisplayName("없는 id로 게시글을 조회하면 ArticleNotExistsException 예외가 발생한다.")
        void ArticleNotExistsExceptionFail() {
            //given
            Long articleId = 100L;

            when(articleRepository.findById(articleId)).thenReturn(Optional.empty()); // Optional.empty() 반환

            //when
            //then
            Exception exception = assertThrows(ArticleNotExistsException.class, () ->
                    articleService.findArticle(principalDetails, articleId));
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.ARTICLE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("게시글 조회수가 1 증가한다.")
        void increaseViewCountSuccess() {
            //given
            Long articleId = 1L;
            Article article = articles.get(0);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

            //when
            articleService.findArticle(principalDetails, articleId);

            //then
            assertThat(article.getViewCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("게시글을 리스트로 조회할 때,")
    class findArticleList {

        @Test
        @DisplayName("기숙사 유형 조건에 맞는 게시글들을 조회한다.")
        public void findArticlesByDormitoryTypeSuccess() {
            //given
            String articleDormitoryType = "양진재";
            ArticleRequest articleRequest = new ArticleRequest("createdAt", "모집중");
            Pageable pageable = PageRequest.of(0, 10);

            SliceImpl<Article> findArticles = new SliceImpl<>(List.of(articles.get(0), articles.get(1)), pageable, false);

            when(articleRepository.findAllByDormitoryTypeAndBoardType(
                    ArticleDormitoryType.fromName(articleDormitoryType), null, articleRequest, pageable))
                    .thenReturn(findArticles);

            //when
            ArticleListResponseDto responseDto = articleService.findAllArticles(principalDetails, articleDormitoryType, articleRequest, pageable);

            //then
            assertThat(responseDto.articles().size()).isEqualTo(2);
            assertThat(responseDto.loginMemberId()).isEqualTo(principalDetails.getMember().getId());
        }

        @Test
        @DisplayName("유효하지 않은 기숙사 유형일 시 InvalidArticleDormitoryTypeException 예외가 발생한다.")
        void InvalidArticleDormitoryTypeExceptionFail() {
            //given
            String articleDormitoryType = "오류재";
            ArticleRequest articleRequest = new ArticleRequest("createdAt", "모집중");
            Pageable pageable = PageRequest.of(0, 10);

            //when
            //then
            Exception exception = assertThrows(InvalidArticleDormitoryTypeException.class, () ->
                    articleService.findAllArticles(principalDetails, articleDormitoryType, articleRequest, pageable));
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.ARTICLE_DORMITORY_TYPE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("유효하지 않은 게시판 유형일 시 InvalidBoardTypeException 예외가 발생한다.")
        void InvalidBoardTypeExceptionFail() {
            //given
            String articleDormitoryType = "양성재";
            String articleBoardType = "예외";
            ArticleRequest articleRequest = new ArticleRequest("createdAt", "모집중");
            Pageable pageable = PageRequest.of(0, 10);

            //when
            //then
            Exception exception = assertThrows(InvalidBoardTypeException.class, () ->
                    articleService.findAllArticles(principalDetails, articleDormitoryType, articleBoardType, articleRequest, pageable));
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.BOARD_TYPE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("검색 키워드가 포함된 게시글들을 조회한다.")
        void findArticlesByKeywordSuccess() {
            //given
            String keyword = "키워드";
            String articleDormitoryType = "양진재";
            Pageable pageable = PageRequest.of(0, 10);
            SliceImpl<Article> findArticles = new SliceImpl<>(List.of(articles.get(0), articles.get(1)), pageable, false);

            when(articleRepository.searchArticles(ArticleDormitoryType.fromName(articleDormitoryType), keyword, pageable))
                    .thenReturn(findArticles);

            //when
            articleService.searchArticles()

            //then

        }


//
//        @Test
//        @DisplayName("")
//        void (){
//            //given
//
//            //when
//
//            //then
//
//        }

    }
}