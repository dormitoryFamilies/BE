package dormitoryfamily.doomz.domain.replyComment.service;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.domain.comment.repository.CommentRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.dto.request.CreateReplyCommentRequestDto;
import dormitoryfamily.doomz.domain.replyComment.dto.response.CreateReplyCommentResponseDto;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.domain.replyComment.exception.ReplyCommentNotExistsException;
import dormitoryfamily.doomz.domain.replyComment.repository.ReplyCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyCommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;

    public CreateReplyCommentResponseDto saveReplyComment(Member member, Long commentId, CreateReplyCommentRequestDto requestDto) {
        Comment comment = getCommentById(commentId);
        ReplyComment replyComment = CreateReplyCommentRequestDto.toEntity(member, comment, requestDto);
        ReplyComment savedReplyComment = replyCommentRepository.save(replyComment);
        return CreateReplyCommentResponseDto.fromEntity(savedReplyComment);
    }

    private Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
    }

    public void removeReplyComment(Member member, Long replyCommentId) {
        //작성자 확인 및 대댓글 수 감소 로직 추가 예정
        ReplyComment replyComment = getReplyCommentById(replyCommentId);
        replyCommentRepository.delete(replyComment);
    }

    private ReplyComment getReplyCommentById(Long replyCommentId){
        return replyCommentRepository.findById(replyCommentId)
                .orElseThrow(ReplyCommentNotExistsException::new);
    }
}

