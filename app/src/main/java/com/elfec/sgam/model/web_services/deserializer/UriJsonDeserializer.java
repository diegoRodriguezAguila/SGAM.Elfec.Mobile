package com.elfec.sgam.model.web_services.deserializer;

import android.net.Uri;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Deserializador para tipo Uri que es abstracto y
 * jackson no lo permite de otra forma
 */
public class UriJsonDeserializer extends JsonDeserializer<Uri> {
    @Override
    public Uri deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        return Uri.parse(jp.getValueAsString());
    }
}
