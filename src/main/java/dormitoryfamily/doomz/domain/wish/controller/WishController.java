package dormitoryfamily.doomz.domain.wish.controller;

import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.wish.service.WishService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{articleId}/wishes")
public class WishController {

    private final WishService wishService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> saveWish(
            @PathVariable Long articleId
    ){
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        wishService.saveWish(member, articleId);
        return ResponseEntity.ok(ResponseDto.created());

    }

}
