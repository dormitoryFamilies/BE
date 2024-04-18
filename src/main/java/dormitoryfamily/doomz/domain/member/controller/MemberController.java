package dormitoryfamily.doomz.domain.member.controller;

import dormitoryfamily.doomz.domain.member.dto.response.NicknameCheckResponseDto;
import dormitoryfamily.doomz.domain.member.service.MemberService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MemberController {

    private final MemberService memberService;

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

    // 화원 가입
//    @PostMapping("/member/signUp")
//    public ResponseEntity<ResponseDto<Void>> signUp(
//            @RequestBody @Valid MemberSignUpRequestDto requestDto,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//
//
//    }

}
