package dormitoryfamily.doomz.domain.roomate.recommendation.controller;

import dormitoryfamily.doomz.domain.roomate.recommendation.dto.RecommendationResponseDto;
import dormitoryfamily.doomz.domain.roomate.recommendation.service.RecommendationService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchings")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/recommendations")
    public ResponseEntity<ResponseDto<RecommendationResponseDto>> suggestCandidates(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        RecommendationResponseDto responseDto = recommendationService.findTopCandidates(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ResponseDto<RecommendationResponseDto>> getRecommendedCandidates(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        RecommendationResponseDto responseDto =
                recommendationService.findRecommendedCandidates(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
