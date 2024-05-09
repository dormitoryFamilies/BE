package dormitoryfamily.doomz.global.chat.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StompErrorDto {
    @JsonProperty("errorMessage")
    private final String errorMessage;

    public StompErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
