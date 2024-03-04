package com.test.portal.account.platform.kafka.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private Map<String, String> headers = new HashMap<>();
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String setHeader(String name, String value) {
        return headers.put(name, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
