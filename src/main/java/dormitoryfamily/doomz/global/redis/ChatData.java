package dormitoryfamily.doomz.global.redis;

public record ChatData(
        Long sender,
        Long roomUUID,
        String message,
        String imageUrl,
        String sentTime
) {

}
