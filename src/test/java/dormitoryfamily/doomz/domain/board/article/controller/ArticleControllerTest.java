package dormitoryfamily.doomz.domain.board.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.board.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.board.article.mock.WithCustomMockUser;
import dormitoryfamily.doomz.domain.board.article.service.ArticleService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithCustomMockUser
    void registerArticle() throws Exception {
        // Given
        ArticleRequestDto requestDto = new ArticleRequestDto(
                "양진재",
                "도와주세요",
                "테스트용 제목입니다.",
                "테스트용 게시글 내용입니다.",
                "#바퀴벌래#무서워",
                List.of("S3/images/abcdefg/123",
                        "S3/images/abcdefg/456",
                        "S3/images/abcdefg/789"));
        CreateArticleResponseDto responseDto = new CreateArticleResponseDto(1L);
        given(articleService.save(any(PrincipalDetails.class), any(ArticleRequestDto.class))).willReturn(responseDto);

        // When & Thenㅇ
        mockMvc.perform(post("/api/articles")
                        .header("AccessToken", "aaaaaaaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.data").exists());

        verify(articleService).save(any(PrincipalDetails.class), any(ArticleRequestDto.class));
    }

    @Test
    @DisplayName("게시글을 등록할 수 있다.")
    @WithMockUser(roles = {"VISITOR"})
    void registerArticleSuccess() throws Exception {

        CreateArticleResponseDto responseDto = new CreateArticleResponseDto(1L);
        ArticleRequestDto requestDto = new ArticleRequestDto(
                "양진재",
                "도와주세요",
                "테스트용 제목입니다.",
                "테스트용 게시글 내용입니다.",
                "#바퀴벌래#무서워",
                List.of("S3/images/abcdefg/123",
                        "S3/images/abcdefg/456",
                        "S3/images/abcdefg/789"));

        when(articleService.save(any(PrincipalDetails.class), any(ArticleRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.articleId").value("1L"));

        verify(articleService, times(1)).save(any(PrincipalDetails.class), any(ArticleRequestDto.class));
    }

    @Test
    @DisplayName("태그 허용 개수를 초과해서 게시글을 등록하면 예외가 발생한다.")
    void exceedTagCountFail() {
        //given
        ArticleRequestDto articleRequestDto = new ArticleRequestDto(
                "가짜재",
                "도와주세요",
                "테스트용 제목입니다.",
                "테스트용 게시글 내용입니다.",
                "#바퀴벌래#무서워#갓생#ㅋㅋㅋ#저녁식사#기말고사",
                List.of("S3/images/"));

        //when
        //then
//        assertThrows(MethodArgumentNotValidException.class, () ->
//                articleService.save(principalDetails, articleRequestDto));
    }

    //
//        @Test
//        @DisplayName("이미지 허용 개수를 초과해서 게시글을 등록하면 예외가 발생한다.")
//        void save() {
//            //given
//
//
//            //when
//
//
//            //then
//
//        }
//
//        @Test
//        @DisplayName("제목 또는 본문이 공백인 상태로 게시글을 등록하면 예외가 발생한다.")
//        void save() {
//            //given
//
//
//            //when
//
//
//            //then
//
//        }

//    @Test
//    @DisplayName("이미지 매개변수를 null로 보내면 NullPointerException 예외가 발생한다.")
//    void NotImageUrlExistFail() {
//        //given
//        ArticleRequestDto articleRequestDto = new ArticleRequestDto(
//                "양진재",
//                "도와주세요",
//                "테스트용 제목입니다.",
//                "테스트용 게시글 내용입니다.",
//                "#바퀴벌래#무서워",
//                null);
//
//        //when
//        //then
//        assertThrows(NullPointerException.class, () ->
//        )
}
