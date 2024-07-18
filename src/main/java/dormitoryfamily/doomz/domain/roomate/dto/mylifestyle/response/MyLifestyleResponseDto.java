package dormitoryfamily.doomz.domain.roomate.dto.mylifestyle.response;

import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;

public record MyLifestyleResponseDto(
        Long myLifeStyleId
) {
    public static MyLifestyleResponseDto fromEntity(MyLifestyle myLifeStyle){
        return new MyLifestyleResponseDto(myLifeStyle.getId());
    }
}
