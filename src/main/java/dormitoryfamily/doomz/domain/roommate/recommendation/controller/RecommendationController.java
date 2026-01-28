package dormitoryfamily.doomz.domain.roommate.recommendation.controller;

import dormitoryfamily.doomz.domain.roommate.recommendation.dto.RecommendationWithExplanationResponseDto;
import dormitoryfamily.doomz.domain.roommate.recommendation.service.RecommendationService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchings")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommendations")
    public ResponseEntity<ResponseDto<RecommendationWithExplanationResponseDto>> suggestCandidates(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        RecommendationWithExplanationResponseDto responseDto =
                recommendationService.findTopCandidatesWithExplanations(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
