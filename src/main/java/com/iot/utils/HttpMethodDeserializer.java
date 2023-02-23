package com.iot.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.http.HttpMethod;

import java.io.IOException;

public class HttpMethodDeserializer extends StdDeserializer<HttpMethod> {

    protected HttpMethodDeserializer() {
        super(HttpMethod.class);
    }

    @Override
    public HttpMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        System.out.println(p.currentToken());
        System.out.println(p.getValueAsString());
        return HttpMethod.valueOf(p.getValueAsString());
    }
}
