package dormitoryfamily.doomz.domain.comment.repository;

import dormitoryfamily.doomz.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "replyComments", type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findAllByArticleIdOrderByCreatedAtAsc(Long articleId);

}
