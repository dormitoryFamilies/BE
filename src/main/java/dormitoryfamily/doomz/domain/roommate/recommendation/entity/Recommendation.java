package dormitoryfamily.doomz.domain.roommate.recommendation.entity;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recommendation_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recommendation", orphanRemoval = true, cascade = REMOVE)
    private final List<Candidate> candidates = new ArrayList<>();

    private LocalDateTime recommendedAt;

    @Builder
    public Recommendation(Member member, LocalDateTime recommendedAt) {
        this.member = member;
        this.recommendedAt = recommendedAt;
    }

    public void updateRecommendedAt() {
        recommendedAt = LocalDateTime.now();
    }

    @PrePersist
    public void init() {
        recommendedAt = LocalDateTime.now();
    }
}
