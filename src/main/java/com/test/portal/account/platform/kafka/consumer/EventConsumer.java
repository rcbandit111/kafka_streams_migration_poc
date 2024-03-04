package com.test.portal.account.platform.kafka.consumer;

import com.test.portal.account.platform.kafka.config.AbstractConsumerSeekAware;
import com.test.portal.account.platform.kafka.config.EventHandler;
import com.test.portal.account.platform.kafka.config.Producer;
import com.test.portal.account.platform.util.DataConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;

@Component
public class EventConsumer<K, V> extends AbstractConsumerSeekAware {
  private final EventHandler identityTransactionsEventHandler;

  public EventConsumer(EventHandler identityTransactionsEventHandler) {
    this.identityTransactionsEventHandler = identityTransactionsEventHandler;
  }

  /**
   * Kafka Listener
   */
  @KafkaListener(id = DataConstants.TRUCK_LISTENER,
          topics = "test",
          groupId = "test-group",
          clientIdPrefix = "prefix",
          containerFactory = DataConstants.FACTORY_NAME)
  public void processEnrichedIdentityTransactions(ConsumerRecord<K, V> consumerRecord) {
    identityTransactionsEventHandler.handle(getTransactionFromConsumerRecord(consumerRecord));
  }

  private SimpleMapMessage getTransactionFromConsumerRecord(ConsumerRecord<K, V> consumerRecord) {
    SimpleMapMessage simpleMapMessage = (SimpleMapMessage) consumerRecord.value();
      setHeaders(simpleMapMessage, consumerRecord);
      return simpleMapMessage;
  }
  
  private void setHeaders(SimpleMapMessage message, ConsumerRecord<K, V> record) {
    message.setHeader("partition",
            String.valueOf(record.partition()));
    message.setHeader("topic",
            record.topic());
    message.setHeader("offset",
            String.valueOf(record.offset()));
    message.setHeader("key",
            Objects.isNull(record.key()) ? null : record.key().toString());
    message.setHeader("producer",
            record.producer());
    message.setHeader("timestamp",
            String.valueOf(record.timestamp()));
  }

  @Bean
  public Producer<String, String> kafkaProducer() {
    final Properties producerproperties = new Properties();
    producerproperties
            .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    producerproperties.put(ProducerConfig.STREAMS_BUFFER_TIME_CONFIG, "200");
    return new Producer<>(producerproperties);
  }
}
