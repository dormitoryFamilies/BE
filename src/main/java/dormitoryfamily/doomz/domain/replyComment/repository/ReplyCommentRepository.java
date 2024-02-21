package dormitoryfamily.doomz.domain.replyComment.repository;

import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

    Optional<ReplyComment> findFirstByCommentId(Long commentId);
}
