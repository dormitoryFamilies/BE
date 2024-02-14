package dormitoryfamily.doomz.global.util.s3.controller;

import dormitoryfamily.doomz.global.util.ResponseDto;
import dormitoryfamily.doomz.global.util.s3.response.S3UploadResponseDto;
import dormitoryfamily.doomz.global.util.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Component
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/api/images")
    public ResponseEntity<ResponseDto<S3UploadResponseDto>> uploadImage(
            @RequestPart("file") MultipartFile multipartFile) throws IOException {

        ResponseDto<S3UploadResponseDto> responseDto = ResponseDto.okWithData(s3Service.saveImage(multipartFile));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/api/images")
    public ResponseEntity<ResponseDto> deleteImage(@RequestParam("imageUrl") String imageUrl) {

        s3Service.removeImage(imageUrl);
        ResponseDto<Void> responseDTO = ResponseDto.ok();
        return ResponseEntity.status(responseDTO.getCode()).body(responseDTO);
    }
}
