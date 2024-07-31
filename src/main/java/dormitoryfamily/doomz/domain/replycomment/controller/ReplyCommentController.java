package dormitoryfamily.doomz.domain.replycomment.controller;

import dormitoryfamily.doomz.domain.replycomment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replycomment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replycomment.service.ReplyCommentService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @PostMapping("/comments/{commentId}/reply-comments")
    public ResponseEntity<ResponseDto<CreateReplyCommentResponseDto>> saveReplyComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CreateReplyCommentRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        CreateReplyCommentResponseDto responseDto = replyCommentService.saveReplyComment(principalDetails, commentId, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @DeleteMapping("/reply-comments/{replyCommentId}")
    public ResponseEntity<ResponseDto<Void>> deleteReplyComment(
            @PathVariable Long replyCommentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        replyCommentService.removeReplyComment(principalDetails, replyCommentId);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
