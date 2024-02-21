package dormitoryfamily.doomz.domain.replyComment.repository;

import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

    boolean existsByCommentId(Long commentId);
}
