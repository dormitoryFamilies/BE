package dormitoryfamily.doomz.domain.roomateWish.controller;

import dormitoryfamily.doomz.domain.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.roomateWish.service.RoommateWishService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoommateWishController {

    private final RoommateWishService roommateWishService;

    @PostMapping("/members/{memberId}/matching-wishes")
    public ResponseEntity<ResponseDto<Void>> saveArticleWish(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        roommateWishService.saveRoommateWish(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/members/{memberId}/matching-wishes")
    public ResponseEntity<ResponseDto<Void>> cancelArticleWish(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        roommateWishService.removeRoommateWish(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/roommate-wishes")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findMyRoommateWishes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {
        MemberProfilePagingListResponseDto memberProfilePagingListResponseDto
                = roommateWishService.findMyRoommateWishes(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(memberProfilePagingListResponseDto));
    }
}
