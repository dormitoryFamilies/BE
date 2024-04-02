package dormitoryfamily.doomz.global.redis;

import java.io.Serializable;

public record ChatEntity(
        Long sender,
        Long roomUUID,
        String message,
        String imageUrl,
        String sentTime
) implements Serializable {

}
