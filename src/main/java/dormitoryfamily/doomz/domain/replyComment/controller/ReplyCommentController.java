package dormitoryfamily.doomz.domain.replyComment.controller;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replyComment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replyComment.service.ReplyCommentService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @PostMapping("/comments/{commentId}/replyComments")
    public ResponseEntity<ResponseDto<CreateReplyCommentResponseDto>> saveReplyComment(
            @PathVariable Long commentId,
            @RequestBody CreateReplyCommentRequestDto requestDto
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        CreateReplyCommentResponseDto responseDto = replyCommentService.saveReplyComment(member, commentId, requestDto);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @DeleteMapping("/replyComments/{replyCommentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @PathVariable Long replyCommentId
    ) {
        // 삭제 예정
        Member member = new Member();
        member.setId(1L);

        replyCommentService.removeReplyComment(member, replyCommentId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

}
