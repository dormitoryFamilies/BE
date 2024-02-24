package dormitoryfamily.doomz.domain.article.controller;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.request.ArticleSearchRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.service.ArticleService;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<ResponseDto<CreateArticleResponseDto>> register(
            @RequestBody @Valid ArticleRequestDto requestDto
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        CreateArticleResponseDto responseDto = articleService.save(member, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<ArticleResponseDto>> findArticle(
            @PathVariable Long articleId
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleResponseDto responseDto = articleService.findArticle(member, articleId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<Void>> modifyArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleRequestDto requestDto
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        articleService.updateArticle(member, articleId, requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ResponseDto<Void>> removeArticle(
            @PathVariable Long articleId
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        articleService.deleteArticle(member, articleId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PutMapping("/articles/{articleId}/status")
    public ResponseEntity<ResponseDto<Void>> changeStatus(
            @PathVariable Long articleId,
            @RequestParam String status
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        articleService.changeStatus(member, articleId, status);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/dormitories/{dormitoryType}/articles")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findAllArticles(
            @PathVariable String dormitoryType,
            @ModelAttribute ArticleRequest request,
            Pageable pageable
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleListResponseDto responseDto =
                articleService.findAllArticles(member, dormitoryType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/dormitories/{dormitoryType}/board-type/{boardType}/articles")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findAllArticlesWithBoardType(
            @PathVariable String dormitoryType,
            @PathVariable String boardType,
            @ModelAttribute ArticleRequest request,
            Pageable pageable
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleListResponseDto responseDto =
                articleService.findAllArticles(member, dormitoryType, boardType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/dormitories/{dormitoryType}/articles/search")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> searchArticles(
            @PathVariable String dormitoryType,
            @ModelAttribute @Valid ArticleSearchRequestDto requestDto,
            Pageable pageable
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleListResponseDto responseDto = articleService.searchArticles(member, dormitoryType, requestDto, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
