package dormitoryfamily.doomz.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnumConverter implements AttributeConverter<Enum<?>, String> {

    private static final String PACKAGE_NAME = "dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.";

    //Enum을 문자열로 변환하여 데이터베이스에 저장
    @Override
    public String convertToDatabaseColumn(Enum<?> attribute) {
        return attribute == null ? null : attribute.getClass().getSimpleName() + ":" + attribute.name();
    }

    //문자열을 다시 Enum 객체로 변환
    @Override
    public Enum<?> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            String[] parts = dbData.split(":");
            String simpleClassName = parts[0];
            String enumValue = parts[1];
            String className = PACKAGE_NAME.concat(simpleClassName);
            Class<?> enumClass = Class.forName(className);
            return Enum.valueOf((Class<Enum>) enumClass, enumValue);
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert String to Enum", e);
        }
    }
}