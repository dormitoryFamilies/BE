package dormitoryfamily.doomz.domain.roomate.controller;

import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request.CreatePreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.service.LifestyleService;
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

    private final LifestyleService lifestyleService;
    private final PreferenceOrderService preferenceOrderService;

    @PostMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> registerMyLifestyle(
            @RequestBody @Valid CreateMyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.saveMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PatchMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> editMyLifestyle(
            @RequestBody @Valid UpdateMyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.updateMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<LifestyleResponseDto>> getMyLifestyle(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        LifestyleResponseDto responseDto =
                lifestyleService.findLifestyle(principalDetails.getMember().getId());
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/lifestyles/members/{memberId}")
    public ResponseEntity<ResponseDto<LifestyleResponseDto>> getLifestyle(
            @PathVariable Long memberId
    ) {
        LifestyleResponseDto responseDto =
                lifestyleService.findLifestyle(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    /**
     * 개발용 삭제 API
     * 실제 서비스시 삭제 예정
     */
    @DeleteMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> deleteMyLifestyle(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.deleteMyLifestyle(principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    /**
     * 개발용 삭제 API
     * 실제 서비스시 삭제 예정
     */
    @DeleteMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<Void>> deleteMyPreferenceOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceOrderService.deleteMyLifestyle(principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PostMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<Void>> setPreferenceOrder(
            @RequestBody @Valid CreatePreferenceOrderRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceOrderService.setPreferenceOrder(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PatchMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<Void>> updatePreferenceOrder(
            @RequestBody CreatePreferenceOrderRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceOrderService.updatePreferenceOrder(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<PreferenceOrderResponseDto>> getMyPreferenceOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PreferenceOrderResponseDto responseDto =
                preferenceOrderService.findPreferenceOrder(principalDetails.getMember().getId());
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/preference-orders/members/{memberId}")
    public ResponseEntity<ResponseDto<PreferenceOrderResponseDto>> getPreferenceOrder(
            @PathVariable Long memberId
    ) {
        PreferenceOrderResponseDto responseDto =
                preferenceOrderService.findPreferenceOrder(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
