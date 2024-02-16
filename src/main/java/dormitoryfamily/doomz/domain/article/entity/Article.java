package dormitoryfamily.doomz.domain.article.entity;

import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;
    private String content;
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Enumerated(EnumType.STRING)
    private ArticleDormitoryType dormitoryType;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String tags;
    private Integer commentCount;
    private Integer viewCount;
    private Integer wishCount;

    @Builder
    public Article(Member member, String title, String content,
                   String thumbnailUrl, StatusType status, ArticleDormitoryType dormitoryType,
                   BoardType boardType, String tags, Integer commentCount, Integer viewCount,
                   Integer wishCount) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.dormitoryType = dormitoryType;
        this.boardType = boardType;
        this.tags = tags;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.wishCount = wishCount;
    }

    public void plusViewCount() {
        viewCount += 1;
    }

    @PrePersist
    private void init() {
        commentCount = 0;
        viewCount = 0;
        wishCount = 0;
    }
}
