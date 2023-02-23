package com.iot.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.http.HttpMethod;

import java.io.IOException;

public class HttpMethodSerializer extends StdSerializer<HttpMethod> {

    protected HttpMethodSerializer() {
        super(HttpMethod.class);
    }

    @Override
    public void serialize(HttpMethod value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.name());
    }
}
