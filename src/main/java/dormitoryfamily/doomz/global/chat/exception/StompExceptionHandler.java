package dormitoryfamily.doomz.global.chat.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class StompExceptionHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public StompErrorDto handleInvalidChatMessageException(InvalidChatMessageException e) {
        return new StompErrorDto(e.getMessage());
    }
}
