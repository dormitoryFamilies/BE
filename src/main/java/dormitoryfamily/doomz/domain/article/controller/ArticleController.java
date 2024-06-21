package dormitoryfamily.doomz.domain.article.controller;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleSearchRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.service.ArticleService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<ResponseDto<CreateArticleResponseDto>> register(
            @RequestBody @Valid ArticleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        CreateArticleResponseDto responseDto = articleService.save(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<ArticleResponseDto>> findArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        ArticleResponseDto responseDto = articleService.findArticle(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<Void>> modifyArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        articleService.updateArticle(principalDetails, articleId, requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<Void>> removeArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        articleService.deleteArticle(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PutMapping("/articles/{articleId}/status")
    public ResponseEntity<ResponseDto<Void>> changeStatus(
            @PathVariable Long articleId,
            @RequestParam String status,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        articleService.changeStatus(principalDetails, articleId, status);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/dormitories/{dormitoryType}/articles")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findAllArticles(
            @PathVariable String dormitoryType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto =
                articleService.findAllArticles(principalDetails, dormitoryType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/dormitories/{dormitoryType}/board-type/{boardType}/articles")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findAllArticlesWithBoardType(
            @PathVariable String dormitoryType,
            @PathVariable String boardType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto =
                articleService.findAllArticles(principalDetails, dormitoryType, boardType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/dormitories/{dormitoryType}/articles/search")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> searchArticles(
            @PathVariable String dormitoryType,
            @ModelAttribute @Valid ArticleSearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto = articleService.searchArticles(principalDetails, dormitoryType, requestDto, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/my/dormitories/{dormitoryType}/board-type/{boardType}/articles")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findMyArticleWishes(
            @PathVariable String dormitoryType,
            @PathVariable String boardType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto = articleService.findMyArticles(principalDetails, dormitoryType, boardType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
