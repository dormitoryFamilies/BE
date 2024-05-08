package dormitoryfamily.doomz.domain.wish.controller;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.wish.dto.WishMemberListResponseDto;
import dormitoryfamily.doomz.domain.wish.service.WishService;
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
public class WishController {

    private final WishService wishService;

    @PostMapping("/articles/{articleId}/wishes")
    public ResponseEntity<ResponseDto<Void>> saveWish(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){

        wishService.saveWish(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @GetMapping("/articles/{articleId}/wishes")
    public ResponseEntity<ResponseDto<WishMemberListResponseDto>> getWishMemberList(
            @PathVariable Long articleId
    ){
        WishMemberListResponseDto responseDto = wishService.getWishMembers(articleId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @DeleteMapping("/articles/{articleId}/wishes")
    public ResponseEntity<ResponseDto<Void>> cancelWish(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){

        wishService.removeWish(principalDetails, articleId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/dormitories/{dormitoryType}/wishes")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findMyArticleWishes(
            @PathVariable String dormitoryType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){

        ArticleListResponseDto responseDto = wishService.findMyArticleWishes(principalDetails, dormitoryType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
