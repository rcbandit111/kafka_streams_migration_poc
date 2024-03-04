package com.test.portal.account.platform.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer")
public class ConsumerProperties {
  private TopicProperties enrichedTransactions;
  private TopicProperties enrichedIdentityTransactions;

  private int pollTimeout;

  @Data
  public static class TopicProperties {
    private boolean enabled;
    private String topics;
    private int concurrency;
    private String groupId;
  }
}
