package dormitoryfamily.doomz.domain.member.follow.controller;

import dormitoryfamily.doomz.domain.member.follow.dto.FollowStatusResponseDto;
import dormitoryfamily.doomz.domain.member.follow.service.FollowService;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follows")
    public ResponseEntity<ResponseDto<Void>> saveFollow(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        followService.saveFollow(principalDetails, memberId);
       return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/{memberId}/followings")
    public ResponseEntity<ResponseDto<Void>> cancelFollow(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        followService.removeFollowing(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @DeleteMapping("/{memberId}/followers")
    public ResponseEntity<ResponseDto<Void>> deleteFollower(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        followService.removeFollower(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/followings")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findFollowings(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.findFollowings(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followings/search")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> searchFollowings(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.searchFollowings(principalDetails, requestDto, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findFollowers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.findFollowers(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers/search")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> searchFollowers(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.searchFollowers(principalDetails, requestDto, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/{memberId}/follow-status")
    public ResponseEntity<ResponseDto<FollowStatusResponseDto>> checkFollowing(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        FollowStatusResponseDto responseDto = followService.checkFollowing(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
