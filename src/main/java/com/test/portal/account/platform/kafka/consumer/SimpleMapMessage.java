package com.test.portal.account.platform.kafka.consumer;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.HashMap;
import java.util.Map;

public class SimpleMapMessage extends Message {

    Map<String, Object> values = new HashMap<>();

    @JsonGetter
    public Map<String, Object> values() {
        return values;
    }

    @JsonAnySetter
    public void put(String key, Object value) {
        values.put(key, value);
    }
}
