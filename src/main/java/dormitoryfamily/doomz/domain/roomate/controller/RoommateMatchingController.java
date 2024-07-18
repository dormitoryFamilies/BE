package dormitoryfamily.doomz.domain.roomate.controller;

import dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.request.MyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.response.MyLifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request.PreferenceLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.service.MyLifestyleService;
import dormitoryfamily.doomz.domain.roomate.service.PreferenceLifestyleService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoommateMatchingController {

    private final MyLifestyleService myLifestyleService;
    private final PreferenceLifestyleService preferenceLifestyleService;

    @PostMapping("/my/lifestyle")
    public ResponseEntity<ResponseDto<MyLifestyleResponseDto>> registerMyLifestyle(
            @RequestBody MyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        MyLifestyleResponseDto responseDto = myLifestyleService.saveMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @PostMapping("/preference/lifestyle")
    public ResponseEntity<ResponseDto<Void>> registerPreferenceLifestyle(
            @RequestBody PreferenceLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        preferenceLifestyleService.savePreferenceLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
