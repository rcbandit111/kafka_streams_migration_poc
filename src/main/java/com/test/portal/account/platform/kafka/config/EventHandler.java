package com.test.portal.account.platform.kafka.config;

import com.test.portal.account.platform.kafka.consumer.SimpleMapMessage;

public interface EventHandler {

    void handle(SimpleMapMessage event);

    void retry(SimpleMapMessage event);

}
