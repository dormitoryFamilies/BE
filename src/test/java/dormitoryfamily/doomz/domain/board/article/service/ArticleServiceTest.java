package dormitoryfamily.doomz.domain.board.article.service;

import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.board.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.board.article.exception.*;
import dormitoryfamily.doomz.domain.board.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.board.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.board.article.service.ArticleService;
import dormitoryfamily.doomz.domain.board.wish.repository.ArticleWishRepository;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
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
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.TestDataHelper.*;
import static dormitoryfamily.doomz.global.exception.ErrorCode.*;
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
    ArticleWishRepository wishRepository;

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
    class WhenRegisterArticle {

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
    class WhenFindOneArticle {

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
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_NOT_EXISTS.getMessage());
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
    class WhenFindArticleList {

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
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_DORMITORY_TYPE_NOT_EXISTS.getMessage());
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
            assertThat(exception.getMessage()).isEqualTo(BOARD_TYPE_NOT_EXISTS.getMessage());
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
            ArticleListResponseDto responseDto = articleService.searchArticles(principalDetails, articleDormitoryType, new SearchRequestDto(keyword), pageable);

            //then
            assertThat(responseDto.articles().size()).isEqualTo(2);
            assertThat(responseDto.loginMemberId()).isEqualTo(principalDetails.getMember().getId());
            assertThat(responseDto.articles().get(0).articleId()).isEqualTo(articles.get(0).getId());
            assertThat(responseDto.articles().get(1).articleId()).isEqualTo(articles.get(1).getId());
        }
    }

    @Nested
    @DisplayName("게시글을 수정할 때,")
    class WhenModifyArticle {

        @Test
        @DisplayName("본문 내용을 성공적으로 변경한다.")
        void editContentSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldContent = target.getContent();
            String newContent = "수정된 본문 내용입니다.";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    target.getTitle(),
                    newContent, // 본문 수정
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldContent).isEqualTo("내용무");
            assertThat(target.getContent()).isEqualTo(newContent);
        }

        @Test
        @DisplayName("제목을 성공적으로 변경한다.")
        void editTitleSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldTitle = target.getTitle();
            String newTitle = "수정된 제목입니다.";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    newTitle, // 제목 수정
                    target.getContent(),
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldTitle).isEqualTo("겨울바다 보고 싶다.");
            assertThat(target.getTitle()).isEqualTo(newTitle);
        }

        @Test
        @DisplayName("게시글 유형을 성공적으로 변경한다.")
        void editArticleTypeSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldBoardType = target.getBoardType().getDescription();
            String newBoardType = "궁금해요";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    newBoardType, // 게시판 유형 수정
                    target.getTitle(),
                    target.getContent(),
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldBoardType).isEqualTo("도와주세요");
            assertThat(target.getBoardType().getDescription()).isEqualTo(newBoardType);
        }

        @Test
        @DisplayName("기숙사 유형을 성공적으로 수정한다.")
        void editDormitoryTypeSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldDormitoryType = target.getDormitoryType().getName();
            String newDormitoryType = "양진재";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    newDormitoryType, // 기숙사 유형 수정
                    target.getBoardType().getDescription(),
                    target.getTitle(),
                    target.getContent(),
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldDormitoryType).isEqualTo("본관");
            assertThat(target.getDormitoryType().getName()).isEqualTo(newDormitoryType);
        }

        @Test
        @DisplayName("태그를 성공적으로 수정한다.")
        void editTagsSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldTags = target.getTags();
            String newTags = "#새로운태그#테스트중";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    target.getTitle(),
                    target.getContent(),
                    newTags, // 태그 수정
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldTags).isEqualTo("#방학");
            assertThat(target.getTags()).isEqualTo(newTags);
        }

        @Test
        @DisplayName("썸네일이 성공적으로 수정된다.")
        void editImagesUrlSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String oldThumbnails = target.getThumbnailUrl();
            List<String> imagesUrls = List.of(
                    "firstImage/s3/changed",
                    "firstImage/s3/2",
                    "firstImage/s3/3"
            );

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    target.getTitle(),
                    target.getContent(),
                    target.getTags(),
                    imagesUrls // 이미지 수정
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.updateArticle(principalDetails, articleId, requestDto);

            //then
            assertThat(oldThumbnails).isEqualTo("S3/images/abc");
            assertThat(target.getThumbnailUrl()).isEqualTo(imagesUrls.get(0));
        }

        @Test
        @DisplayName("해당 게시글이 없을 때 ArticleNotExistsException 예외가 발생한다.")
        void ArticleNotExistsExceptionFail() {
            //given
            Long articleId = 100L;
            Article target = articles.get(0);

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    "새로운 제목",
                    "새로운 본문 내용",
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.empty()); // Optional.empty() 반환

            //when
            //then
            Exception exception = assertThrows(ArticleNotExistsException.class, () ->
                    articleService.updateArticle(principalDetails, articleId, requestDto));
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("다른 작성자의 게시글을 수정하려고 할 때 InvalidMemberAccessException 예외가 발생한다.")
        void InvalidMemberAccessExceptionFail() {
            //given
            Long articleId = 2L; // 작성자 이순신
            Article target = articles.get(1);

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    target.getBoardType().getDescription(),
                    "새로운 제목",
                    "새로운 본문 내용",
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidMemberAccessException.class, () ->
                    articleService.updateArticle(principalDetails, articleId, requestDto));
            assertThat(exception.getMessage()).isEqualTo(INVALID_MEMBER_ACCESS.getMessage());
        }

        @Test
        @DisplayName("유효하지 않은 게시판 유형일 시 InvalidBoardTypeException 예외가 발생한다.")
        void InvalidBoardTypeExceptionFail() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String wrongBoardType = "에러게시판";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    target.getDormitoryType().getName(),
                    wrongBoardType, // 잘못된 게시판 유형
                    "새로운 제목",
                    "새로운 본문 내용",
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidBoardTypeException.class, () ->
                    articleService.updateArticle(principalDetails, articleId, requestDto));
            assertThat(exception.getMessage()).isEqualTo(BOARD_TYPE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("유효하지 않은 기숙사 유형일 시 InvalidArticleDormitoryTypeException 예외가 발생한다.")
        void InvalidArticleDormitoryTypeExceptionFail() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String wrongDormitoryType = "에러기숙사";

            ArticleRequestDto requestDto = new ArticleRequestDto(
                    wrongDormitoryType, // 잘못된 기숙사 유형
                    target.getBoardType().getDescription(),
                    "새로운 제목",
                    "새로운 본문 내용",
                    target.getTags(),
                    target.getArticleImages().stream().map(ArticleImage::getImageUrl).collect(Collectors.toList())
            );
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidArticleDormitoryTypeException.class, () ->
                    articleService.updateArticle(principalDetails, articleId, requestDto));
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_DORMITORY_TYPE_NOT_EXISTS.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 상태를 변경할 때,")
    class WhenModifyArticleStatus {

        @Test
        @DisplayName("게시글 상태를 성공적으로 변경한다.")
        void editArticleStatusSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            StatusType oldStatus = target.getStatus();
            String newStatus = "모집완료";

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.changeStatus(principalDetails, articleId, newStatus);

            //then
            assertThat(oldStatus.getDescription()).isEqualTo("모집중");
            assertThat(target.getStatus().getDescription()).isEqualTo(newStatus);
        }

        @Test
        @DisplayName("해당 게시글이 존재하지 않으면 ArticleNotExistsException 예외가 발생한다.")
        void ArticleNotExistsExceptionFail() {
            //given
            Long articleId = 100L;
            String newStatus = "모집완료";

            when(articleRepository.findById(articleId)).thenReturn(Optional.empty()); // Optional.empty() 반환

            //when
            //then
            Exception exception = assertThrows(ArticleNotExistsException.class, () ->
                    articleService.changeStatus(principalDetails, articleId, newStatus));
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("다른 작성자가 변경을 시도할 경우 InvalidMemberAccessException 예외가 발생한다.")
        void InvalidMemberAccessExceptionFail() {
            //given
            Long articleId = 2L; // 작성자 이순신
            Article target = articles.get(1);
            String newStatus = "모집완료";

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidMemberAccessException.class, () ->
                    articleService.changeStatus(principalDetails, articleId, newStatus));
            assertThat(exception.getMessage()).isEqualTo(INVALID_MEMBER_ACCESS.getMessage());
        }

        @Test
        @DisplayName("유효하지 않은 상태로 시도하면 InvalidStatusTypeException 예외가 발생한다.")
        void InvalidStatusTypeExceptionFail() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String wrongStatus = "에러상태";

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidStatusTypeException.class, () ->
                    articleService.changeStatus(principalDetails, articleId, wrongStatus));
            assertThat(exception.getMessage()).isEqualTo(STATUS_TYPE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("동일한 상태로 중복 변경을 시도하면 StatusAlreadySetException 예외가 발생한다.")
        void StatusAlreadySetExceptionFail() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);
            String newStatus = target.getStatus().getDescription();

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(StatusAlreadySetException.class, () ->
                    articleService.changeStatus(principalDetails, articleId, newStatus));
            assertThat(exception.getMessage()).isEqualTo("상태 코드는 이미 [" + newStatus + "] 입니다");
        }
    }

    @Nested
    @DisplayName("게시글을 삭제할 때,")
    class WhenDeleteArticle{

        @Test
        @DisplayName("성공적으로 삭제한다.")
        void deleteArticleSuccess() {
            //given
            Long articleId = 1L;
            Article target = articles.get(0);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            articleService.deleteArticle(principalDetails, articleId);

            //then
            verify(articleRepository, times(1)).delete(target);
        }

        @Test
        @DisplayName("해당 게시글이 존재하지 않으면 ArticleNotExistsException 예외가 발생한다.")
        void ArticleNotExistsExceptionFail() {
            //given
            Long articleId = 100L;

            when(articleRepository.findById(articleId)).thenReturn(Optional.empty()); // Optional.empty() 반환

            //when
            //then
            Exception exception = assertThrows(ArticleNotExistsException.class, () ->
                    articleService.deleteArticle(principalDetails, articleId));
            assertThat(exception.getMessage()).isEqualTo(ARTICLE_NOT_EXISTS.getMessage());
        }

        @Test
        @DisplayName("다른 작성자가 삭제를 시도할 경우 InvalidMemberAccessException 예외가 발생한다.")
        void InvalidMemberAccessExceptionFail() {
            //given
            Long articleId = 2L;
            Article target = articles.get(1);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(target));

            //when
            //then
            Exception exception = assertThrows(InvalidMemberAccessException.class, () ->
                    articleService.deleteArticle(principalDetails, articleId));
            assertThat(exception.getMessage()).isEqualTo(INVALID_MEMBER_ACCESS.getMessage());
        }
    }
}