package dormitoryfamily.doomz.domain.roomate.controller;

import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.service.MyLifestyleService;
import dormitoryfamily.doomz.domain.roomate.service.PreferenceOrderService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoommateMatchingController {

    private final MyLifestyleService myLifestyleService;
    private final PreferenceOrderService preferenceOrderService;

    @PostMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> registerMyLifestyle(
            @RequestBody @Valid CreateMyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        myLifestyleService.saveMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PatchMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> editMyLifestyle(
            @RequestBody @Valid UpdateMyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        myLifestyleService.updateMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<LifestyleResponseDto>> getMyLifestyle(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        LifestyleResponseDto responseDto =
                myLifestyleService.findMyLifestyle(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    /**
     * 선호 우선순위의 등록, 수정 모두 사용
     */
    @PostMapping("/my/preferences/lifestyles")
    public ResponseEntity<ResponseDto<Void>> setPreferenceOrder(
            @RequestBody @Valid PreferenceOrderRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        preferenceOrderService.setPreferenceOrder(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/preferences/lifestyles")
    public ResponseEntity<ResponseDto<PreferenceOrderResponseDto>> getMyPreferenceOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        PreferenceOrderResponseDto responseDto =
                preferenceOrderService.findPreferenceOrder(principalDetails.getMember().getId());
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/preferences/lifestyles/{memberId}")
    public ResponseEntity<ResponseDto<PreferenceOrderResponseDto>> getPreferenceOrder(
            @PathVariable Long memberId
    ) {

        PreferenceOrderResponseDto responseDto =
                preferenceOrderService.findPreferenceOrder(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
