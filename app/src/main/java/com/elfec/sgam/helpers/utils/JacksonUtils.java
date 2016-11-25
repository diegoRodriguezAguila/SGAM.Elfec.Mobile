package com.elfec.sgam.helpers.utils;

import android.net.Uri;

import com.elfec.sgam.model.web_services.deserializer.EnumJsonDeserializer;
import com.elfec.sgam.model.web_services.deserializer.UriJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.lang.ref.SoftReference;

/**
 * Created by drodriguez on 17/11/2016.
 * utils for Jackson json serializer
 */

public class JacksonUtils {

    /**
     * ObjectMapper caché
     */
    private static SoftReference<ObjectMapper> sObjectMapperCache;

    public static ObjectMapper generateMapper() {
        if (sObjectMapperCache != null && sObjectMapperCache.get() != null) {
            return sObjectMapperCache.get();
        }
        sObjectMapperCache = new SoftReference<>(new ObjectMapper().setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule().addDeserializer(Uri.class, new
                        UriJsonDeserializer())
                        .setDeserializerModifier(new BeanDeserializerModifier() {
                            @Override
                            public JsonDeserializer<Enum<?>> modifyEnumDeserializer
                                    (DeserializationConfig config, final JavaType type,
                                     BeanDescription beanDesc, final JsonDeserializer<?>
                                             deserializer) {
                                return new EnumJsonDeserializer(type);
                            }
                        })));
        return sObjectMapperCache.get();
    }
}
