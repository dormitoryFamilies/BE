package dormitoryfamily.doomz.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnumConverter implements AttributeConverter<Enum<?>, String> {

    @Override
    public String convertToDatabaseColumn(Enum<?> attribute) {
        return attribute == null ? null : attribute.getClass().getName() + ":" + attribute.name();
    }

    @Override
    public Enum<?> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            String[] parts = dbData.split(":");
            String className = parts[0];
            String enumValue = parts[1];
            Class<?> enumClass = Class.forName(className);
            return Enum.valueOf((Class<Enum>) enumClass, enumValue);
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert String to Enum", e);
        }
    }
}