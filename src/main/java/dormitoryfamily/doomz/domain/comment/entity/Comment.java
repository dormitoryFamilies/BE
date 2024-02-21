package dormitoryfamily.doomz.domain.comment.entity;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "comment")
    @OrderBy("createdAt ASC")
    private List<ReplyComment> replyComments  = new ArrayList<>();

    private boolean isDeleted;

    @Builder
    public Comment(Article article, Member member, String content, boolean isDeleted) {
        this.article = article;
        this.member = member;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void markAsDeleted(){
        this.isDeleted = true;
    }
}
