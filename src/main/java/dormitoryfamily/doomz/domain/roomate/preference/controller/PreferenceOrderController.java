package dormitoryfamily.doomz.domain.roomate.preference.controller;

import dormitoryfamily.doomz.domain.roomate.preference.dto.request.PreferenceOrderRequestDto;
import dormitoryfamily.doomz.domain.roomate.preference.dto.response.PreferenceOrderResponseDto;
import dormitoryfamily.doomz.domain.roomate.preference.service.PreferenceOrderService;
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
public class PreferenceOrderController {

    private final PreferenceOrderService preferenceOrderService;

    @PostMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<Void>> setPreferenceOrder(
            @RequestBody @Valid PreferenceOrderRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceOrderService.setPreferenceOrders(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PutMapping("/my/preference-orders")
    public ResponseEntity<ResponseDto<Void>> updatePreferenceOrder(
            @RequestBody @Valid PreferenceOrderRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceOrderService.updatePreferenceOrders(requestDto, principalDetails);
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

    @GetMapping("/members/{memberId}/preference-orders")
    public ResponseEntity<ResponseDto<PreferenceOrderResponseDto>> getPreferenceOrder(
            @PathVariable Long memberId
    ) {
        PreferenceOrderResponseDto responseDto =
                preferenceOrderService.findPreferenceOrder(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
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
}
