package dormitoryfamily.doomz.domain.articleWish.controller;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.articleWish.service.ArticleWishService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleWishController {

    private final ArticleWishService articleWishService;

    @PostMapping("/articles/{articleId}/wishes")
    public ResponseEntity<ResponseDto<Void>> saveArticleWish(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        articleWishService.saveArticleWish(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @GetMapping("/articles/{articleId}/wish-members")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> findArticleWishMemberList(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        MemberProfileListResponseDto responseDto = articleWishService.findArticleWishMembers(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @DeleteMapping("/articles/{articleId}/wishes")
    public ResponseEntity<ResponseDto<Void>> cancelArticleWish(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        articleWishService.removeArticleWish(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/dormitories/{dormitoryType}/wishes")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findMyArticleWishes(
            @PathVariable String dormitoryType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto = articleWishService.findMyArticleWishes(principalDetails, dormitoryType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
