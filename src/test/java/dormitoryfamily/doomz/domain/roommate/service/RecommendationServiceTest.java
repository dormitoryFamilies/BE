package dormitoryfamily.doomz.domain.roommate.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.domain.roommate.matching.exception.AlreadyMatchedMemberException;
import dormitoryfamily.doomz.domain.roommate.matching.service.MatchingRequestService;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.preference.exception.PreferenceOrderNotExistsException;
import dormitoryfamily.doomz.domain.roommate.preference.repository.PreferenceOrderRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.dto.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Candidate;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;
import dormitoryfamily.doomz.domain.roommate.recommendation.exception.RecommendationNotExistsException;
import dormitoryfamily.doomz.domain.roommate.recommendation.exception.TooManyRequestException;
import dormitoryfamily.doomz.domain.roommate.recommendation.repository.CandidateRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.repository.RecommendationRepository;
import dormitoryfamily.doomz.domain.roommate.recommendation.service.RecommendationService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dormitoryfamily.doomz.TestDataHelper.*;
import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.RECOMMENDATIONS_MAX_COUNT;
import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.RECOMMENDATION_INTERVAL_HOURS;
import static dormitoryfamily.doomz.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @InjectMocks
    private RecommendationService recommendationService;

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PreferenceOrderRepository preferenceOrderRepository;

    @Mock
    private LifestyleRepository lifestyleRepository;

    @Mock
    private MatchingRequestService matchingRequestService;

    private PrincipalDetails principalDetails;
    private Map<String, Member> members;
    private List<Lifestyle> lifestyles;
    private List<PreferenceOrder> preferenceOrders;
    private Recommendation recommendation;
    private List<Candidate> candidates;

    @BeforeEach
    public void setUp() {
        //Mockito 어노테이션 초기화
        MockitoAnnotations.openMocks(this);

        members = createDummyMembers();
        lifestyles = createDummyLifestyles(members);
        preferenceOrders = createDummyPreferenceOrders(members);
        recommendation = createDummyRecommendation(1L, members.get("홍길동"));
        candidates = createDummyCandidates(recommendation, members);


        //로그인한 사용자 설정 = 홍길동
        principalDetails = new PrincipalDetails(members.get("홍길동"));

        //preferenceOrderRepository.findByMember 호출 시 PreferenceOrder 객체를 반환하도록 설정
        when(preferenceOrderRepository.findByMember(any(Member.class)))
                .thenAnswer(invocation -> {
                    Member member = invocation.getArgument(0);
                    return preferenceOrders.stream()
                            .filter(p -> p.getMember().equals(member))
                            .findFirst();
                });

        //lifestyleRepository.findByMemberId 호출 시 lifestyle 객체를 반환하도록 설정
        when(lifestyleRepository.findByMemberId(any(Long.class)))
                .thenAnswer(invocation -> {
                    Long memberid = invocation.getArgument(0);
                    return lifestyles.stream()
                            .filter(l -> l.getMember().getId().equals(memberid))
                            .findFirst();
                });

        //lifestyleRepository.findAllExcludingMember 호출 시 List<lifestyle> 객체를 반환하도록 설정
        when(lifestyleRepository.findAllExcludingMember(any(Member.class)))
                .thenAnswer(invocation -> {
                    Member member = invocation.getArgument(0);
                    return lifestyles.stream()
                            .filter(l -> !l.getMember().equals(member)
                                    && l.getMember().getGenderType().equals(member.getGenderType())
                                    && l.getMember().getDormitoryType().equals(member.getDormitoryType())
                                    && !l.getMember().isRoommateMatched())
                            .toList();
                });

        //memberRepository.findById 호출 Member 객체를 반환하도록 설정
        when(memberRepository.findById(any(Long.class)))
                .thenAnswer(invocation -> {
                    Long memberId = invocation.getArgument(0);
                    return members.values().stream()
                            .filter(m -> m.getId().equals(memberId))
                            .findFirst();
                });
    }

    @Test
    @DisplayName("총점이 가장 높은 사람 순서대로 조회할 수 있다.")
    void findTopCandidateSuccess() {
        //given
        Member loginMember = principalDetails.getMember();
        LocalDateTime recommendedAt = LocalDateTime.now().minusHours(RECOMMENDATION_INTERVAL_HOURS + 1);
        Recommendation recommendation = new Recommendation(loginMember, recommendedAt);
        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));

        //when
        RecommendationResponseDto responseDto = recommendationService.findTopCandidates(principalDetails);
        List<Long> candidateIds = responseDto.candidateIds();

        //then
        verify(candidateRepository, times(1)).deleteAllByRecommendation(any(Recommendation.class));
        verify(candidateRepository, times(1)).saveAll(anyList());
        assertEquals(candidateIds.size(), 4);
        assertEquals(candidateIds.get(0), 4L);
        assertEquals(candidateIds.get(1), 9L);
        assertEquals(candidateIds.get(2), 7L);
        assertEquals(candidateIds.get(3), 6L);
    }

    @Test
    @DisplayName("추천 받은 이후 허용 시간이 지나지 않으면 TooManyRequestException 예외가 발생한다.")
    void tooManyRequestExceptionFail() {
        //given
        Member loginMember = principalDetails.getMember();
        Recommendation recommendation = new Recommendation(loginMember, LocalDateTime.now());

        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));

        //when
        //then
        TooManyRequestException exception = assertThrows(TooManyRequestException.class, () -> {
            recommendationService.findTopCandidates(principalDetails);
        });
        assertThat(exception.getMessage()).isEqualTo(TOO_MANY_REQUEST.getMessage());
    }

    @Test
    @DisplayName("로그인 사용자가 이미 매칭이 되어 있는 경우 AlreadyMatchedMemberException 예외가 발생한다.")
    void alreadyMatchedMemberExceptionFail() {
        //given
        principalDetails = new PrincipalDetails(members.get("진혁")); //isRoommateMatched 가 true
        Member loginMember = principalDetails.getMember();
        LocalDateTime recommendedAt = LocalDateTime.now().minusHours(RECOMMENDATION_INTERVAL_HOURS + 1);
        Recommendation recommendation = new Recommendation(loginMember, recommendedAt);
        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));

        //when
        //then
        AlreadyMatchedMemberException exception = assertThrows(AlreadyMatchedMemberException.class, () -> {
            recommendationService.findTopCandidates(principalDetails);
        });
        assertThat(exception.getMessage()).isEqualTo(ALREADY_MATCHED_MEMBER.getMessage());
    }


    @Test
    @DisplayName("로그인 사용자가 선호 우선순위를 설정하지 않은 경우 PreferenceOrderNotExistsException 예외가 발생한다.")
    void preferenceOrderNotExistsExceptionFail() {
        //given
        principalDetails = new PrincipalDetails(members.get("민재")); //isRoommateMatched 가 true
        Member loginMember = principalDetails.getMember();
        LocalDateTime recommendedAt = LocalDateTime.now().minusHours(RECOMMENDATION_INTERVAL_HOURS + 1);
        Recommendation recommendation = new Recommendation(loginMember, recommendedAt);
        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));

        //when
        //then
        PreferenceOrderNotExistsException exception = assertThrows(PreferenceOrderNotExistsException.class, () -> {
            recommendationService.findTopCandidates(principalDetails);
        });
        assertThat(exception.getMessage()).isEqualTo(PREFERENCE_ORDER_NOT_EXISTS.getMessage());
    }

    @Test
    @DisplayName("로그인 사용자가 라이프 스타일을 설정하지 않은 경우 LifestyleNotExistsException 예외가 발생한다.")
    void lifestyleNotExistsExceptionFail() {
        //given
        principalDetails = new PrincipalDetails(members.get("서진")); //isRoommateMatched 가 true
        Member loginMember = principalDetails.getMember();
        LocalDateTime recommendedAt = LocalDateTime.now().minusHours(RECOMMENDATION_INTERVAL_HOURS + 1);
        Recommendation recommendation = new Recommendation(loginMember, recommendedAt);
        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));

        //when
        //then
        LifestyleNotExistsException exception = assertThrows(LifestyleNotExistsException.class, () -> {
            recommendationService.findTopCandidates(principalDetails);
        });
        assertThat(exception.getMessage()).isEqualTo(LIFESTYLE_NOT_EXISTS.getMessage());
    }

    @Test
    @DisplayName("추천된 회원 아이디를 성공적으로 조회할 수 있다.")
    void getRecommendedCandidatesListSuccess() {
        //given
        Member loginMember = principalDetails.getMember();

        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.of(recommendation));
        when(candidateRepository.findAllByRecommendationIdOrderByCandidateScoreDesc(any(Long.class)))
                .thenAnswer(invocation -> {
                    Long recommendationId = invocation.getArgument(0);
                    return candidates.stream()
                            .filter(c -> c.getRecommendation().getId().equals(recommendationId))
                            .sorted(Comparator.comparing(Candidate::getCandidateScore).reversed())
                            .limit(RECOMMENDATIONS_MAX_COUNT)
                            .toList();
                });

        //when
        RecommendationResponseDto responseDto = recommendationService.findRecommendedCandidates(principalDetails);
        List<Long> candidateIds = responseDto.candidateIds();

        //then
        assertThat(candidateIds.size()).isEqualTo(5);
        assertThat(candidateIds.get(0)).isEqualTo(21L);
        assertThat(candidateIds.get(1)).isEqualTo(17L);
        assertThat(candidateIds.get(2)).isEqualTo(18L);
        assertThat(candidateIds.get(3)).isEqualTo(16L);
        assertThat(candidateIds.get(4)).isEqualTo(24L);
    }

    @Test
    @DisplayName("요청을 한 적이 없으면 RecommendationNotExistsException 예외가 발생한다.")
    void recommendationNotExistsExceptionFail() {
        //given
        Member loginMember = principalDetails.getMember();

        when(recommendationRepository.findByMemberId(loginMember.getId())).thenReturn(Optional.empty());

        //when
        //then
        RecommendationNotExistsException exception = assertThrows(RecommendationNotExistsException.class, () ->
                recommendationService.findRecommendedCandidates(principalDetails));
        assertThat(exception.getMessage()).isEqualTo(RECOMMENDATION_NOT_EXISTS.getMessage());
    }
}