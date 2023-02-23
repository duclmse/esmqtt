package com.iot.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Accessors(fluent = true)
public class ApiCallEvent extends ApplicationEvent {

    private final HttpMethod method;
    private final String url;
    private final String body;

    public ApiCallEvent(Object source, HttpMethod method, String url, String body) {
        super(source);
        this.method = method;
        this.url = url;
        this.body = body;
    }
}
