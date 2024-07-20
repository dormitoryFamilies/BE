package dormitoryfamily.doomz.domain.roomate.controller;

import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.lifestyle.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request.PreferenceLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.service.MyLifestyleService;
import dormitoryfamily.doomz.domain.roomate.service.PreferenceLifestyleService;
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
    private final PreferenceLifestyleService preferenceLifestyleService;

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

    @PostMapping("/preference/lifestyles")
    public ResponseEntity<ResponseDto<Void>> registerPreferenceLifestyle(
            @RequestBody PreferenceLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceLifestyleService.savePreferenceLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
