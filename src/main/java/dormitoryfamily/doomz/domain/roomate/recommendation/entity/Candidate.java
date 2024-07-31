package dormitoryfamily.doomz.domain.roomate.recommendation.entity;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Candidate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "candidate_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    @OneToOne
    @JoinColumn(name = "candidate_member_id")
    private Member candidateMember;

    private Double candidateScore;

    @Builder
    public Candidate(Recommendation recommendation, Member candidateMember, Double candidateScore) {
        this.recommendation = recommendation;
        this.candidateMember = candidateMember;
        this.candidateScore = candidateScore;
    }
}
