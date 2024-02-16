package dormitoryfamily.doomz.domain.article.controller;

import dormitoryfamily.doomz.domain.article.dto.request.CreateArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.service.ArticleService;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<ResponseDto<CreateArticleResponseDto>> register(
            @RequestBody CreateArticleRequestDto requestDto
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        CreateArticleResponseDto responseDto = articleService.save(member, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("articles/{articleId}")
    public ResponseEntity<ResponseDto<ArticleResponseDto>> findArticle(
            @PathVariable Long articleId
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleResponseDto responseDto = articleService.findArticle(member, articleId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

}
