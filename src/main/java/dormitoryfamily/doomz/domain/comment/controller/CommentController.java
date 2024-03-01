package dormitoryfamily.doomz.domain.comment.controller;

import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<ResponseDto<CreateCommentResponseDto>> saveComment(
            @PathVariable Long articleId,
            @RequestBody @Valid CreateCommentRequestDto requestDto
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        CreateCommentResponseDto responseDto = commentService.saveComment(articleId, member, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<ResponseDto<CommentListResponseDto>> getComments(
            @PathVariable Long articleId
    ){
        //삭제 예정
        Member member = new Member();
        member.setId(1L);

        CommentListResponseDto responseDto= commentService.getCommentList(articleId, member);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @PathVariable Long commentId
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        commentService.removeComment(member, commentId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/comments")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findMyArticleWishes(
            @RequestParam String dormitoryType,
            @RequestParam(required = false) String boardType,
            Pageable pageable
    ){
        //삭제 예정
        Member member = new Member();
        member.setId(1L);

        ArticleListResponseDto responseDto = commentService.findMyComments(member, dormitoryType, boardType, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
