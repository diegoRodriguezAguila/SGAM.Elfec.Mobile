package com.elfec.sgam.local_storage.serializers;

import android.net.Uri;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Kryo uri serializer
 */
public class KryoUriSerializer extends Serializer<Uri> {
    @Override
    public void write(Kryo kryo, Output output, Uri uri) {
        output.writeString(uri.toString());
    }
    @Override
    public Uri read(Kryo kryo, Input input, Class<Uri> type) {
        return Uri.parse(input.readString());
    }
}
