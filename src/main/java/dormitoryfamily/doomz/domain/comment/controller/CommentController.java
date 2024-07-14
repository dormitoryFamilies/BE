package dormitoryfamily.doomz.domain.comment.controller;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.request.CreateCommentRequestDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CommentListResponseDto;
import dormitoryfamily.doomz.domain.comment.dto.response.CreateCommentResponseDto;
import dormitoryfamily.doomz.domain.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<ResponseDto<CreateCommentResponseDto>> saveComment(
            @PathVariable Long articleId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        CreateCommentResponseDto responseDto = commentService.saveComment(articleId, principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<ResponseDto<CommentListResponseDto>> getComments(
            @PathVariable Long articleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        CommentListResponseDto responseDto = commentService.getCommentList(articleId, principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        commentService.removeComment(principalDetails, commentId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/dormitories/{dormitoryType}/board-types/{boardType}/comments")
    public ResponseEntity<ResponseDto<ArticleListResponseDto>> findMyComments(
            @PathVariable String dormitoryType,
            @PathVariable String boardType,
            @ModelAttribute ArticleRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {

        ArticleListResponseDto responseDto = commentService.findMyComments(principalDetails, dormitoryType, boardType, request, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
