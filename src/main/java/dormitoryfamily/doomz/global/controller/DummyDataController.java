package dormitoryfamily.doomz.global.controller;

import dormitoryfamily.doomz.global.config.DataInitializer;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DummyDataController {
    private final DataInitializer dataInitializer;
    
    @PostMapping("/dev/dummy-data")
    public ResponseEntity<ResponseDto<Void>> createDummyData() {
        dataInitializer.createAndSaveDummyData();
        return ResponseEntity.ok(ResponseDto.created());
    }
}
