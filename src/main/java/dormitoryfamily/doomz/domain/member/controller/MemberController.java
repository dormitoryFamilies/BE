package dormitoryfamily.doomz.domain.member.controller;

import dormitoryfamily.doomz.domain.member.dto.request.MemberSetUpProfileRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.*;
import dormitoryfamily.doomz.domain.member.service.MemberService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MyProfileResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> findAllMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        MemberProfileListResponseDto responseDto = memberService.findAllMembers(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("members/{memberId}")
    public ResponseEntity<ResponseDto<MemberDetailsResponseDto>> findMemberProfile(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails

    ) {
        MemberDetailsResponseDto responseDto = memberService.getMemberProfile(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/my/profile")
    public ResponseEntity<ResponseDto<MyProfileResponseDto>> getMyProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        MyProfileResponseDto responseDto = memberService.getMyProfile(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PutMapping("/my/profile")
    public ResponseEntity<ResponseDto<Void>> modifyMyProfile(
            @RequestBody MyProfileModifyRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        memberService.modifyMyProfile(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/members/search")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> searchMembers(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        MemberProfileListResponseDto responseDto = memberService.searchMembers(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    // 닉네임 중복 체크
    @GetMapping("/members/check")
    public ResponseEntity<ResponseDto<NicknameCheckResponseDto>> checkNickname(
            @RequestParam
            @Length(min = 1, max = 10, message = "최소 1자, 최대 10자 입니다.")
            @NotBlank(message = "공백은 유효하지 않습니다.")
            @Pattern(regexp = "^[^\\s].*", message = "닉네임의 앞부분에 공백이 올 수 없습니다.")
            String nickname
    ) {

        return ResponseEntity.ok(
                ResponseDto.okWithData(memberService.checkNickname(nickname)));
    }

    @PutMapping("/members/initial-profiles")
    public ResponseEntity<ResponseDto<Void>> setUpProfile(
            @RequestBody @Valid MemberSetUpProfileRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        memberService.setUpProfile(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
