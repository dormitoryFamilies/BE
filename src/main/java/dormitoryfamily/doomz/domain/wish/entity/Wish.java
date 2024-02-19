package dormitoryfamily.doomz.domain.wish.entity;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public Wish(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
