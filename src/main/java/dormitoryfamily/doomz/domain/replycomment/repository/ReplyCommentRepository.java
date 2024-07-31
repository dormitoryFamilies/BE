package dormitoryfamily.doomz.domain.replycomment.repository;

import dormitoryfamily.doomz.domain.replycomment.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

    boolean existsByCommentId(Long commentId);

    List<ReplyComment> findAllByMemberId(Long id);

}
