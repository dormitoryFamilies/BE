package dormitoryfamily.doomz.domain.roommate.wish.controller;

import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.roommate.wish.service.RoommateWishService;
import dormitoryfamily.doomz.domain.roommate.wish.dto.response.RoommateWishStatusResponseDto;
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

    @PostMapping("/members/{memberId}/roommate-wishes")
    public ResponseEntity<ResponseDto<Void>> saveArticleWish(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        roommateWishService.saveRoommateWish(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/members/{memberId}/roommate-wishes")
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

    @GetMapping("/members/{memberId}/roommate-wishes")
    public ResponseEntity<ResponseDto<RoommateWishStatusResponseDto>> getRoommateWishStatus(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long memberId
    ){
        RoommateWishStatusResponseDto responseDto = roommateWishService.getRoommateWishStatus(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
