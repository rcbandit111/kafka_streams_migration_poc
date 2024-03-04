package com.test.portal.account.platform.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Properties;

public class Producer<K, V> {
  private final Logger logger = LoggerFactory.getLogger(Producer.class);
  private final KafkaProducer<K, V> kproducer;

  public Producer(Properties properties) {
    final Properties baseProperties = new Properties();
    baseProperties.putAll(getBaseProperties());
    baseProperties.putAll(properties);
    kproducer = new KafkaProducer<>(baseProperties);
  }

  /**
   * Creates Producer with default properties.
   */
  public Producer() {
    kproducer = new KafkaProducer<>(getBaseProperties());
  }

  private Properties getBaseProperties() {
    final Properties baseProperties = new Properties();
    try {
      baseProperties.load(getClass().getClassLoader().getResourceAsStream("producer.properties"));
    } catch (final IOException e) {
      logger.error("Cannot read producer.properties", e);
    }
    return baseProperties;
  }

  @PreDestroy
  public void closeProducer() {
    if (kproducer != null) {
      logger.info("Flushing producer");
      kproducer.flush();

      logger.info("Closing producer");
      kproducer.close();
    }
  }
}
