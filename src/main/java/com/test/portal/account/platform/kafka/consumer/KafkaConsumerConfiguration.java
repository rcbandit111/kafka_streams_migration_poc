package com.test.portal.account.platform.kafka.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "message-consumer")
public class KafkaConsumerConfiguration {

  private TopicProperties transactionSearchRetryStream;
  private TopicProperties identityTransactionSearchRetryStream;

  @Data
  public static class TopicProperties {
    private String topic;
  }
}
