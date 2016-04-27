package com.elfec.sgam.model.web_services.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Enum json deserializer
 */
public class EnumJsonDeserializer extends JsonDeserializer<Enum<?>> {
    private JavaType type;

    public EnumJsonDeserializer(JavaType type){
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enum<?> deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonToken curr = jp.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
            Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
            return Enum.valueOf(rawClass, jp.getValueAsString().toUpperCase());
        }
        if (curr == JsonToken.VALUE_NUMBER_INT) {
            int index = jp.getIntValue();
            Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
            int i = 0;
            for (Enum option : rawClass.getEnumConstants()) {
                if(i==index)
                    return option;
                i++;
            }
        }
        return null;
    }
}