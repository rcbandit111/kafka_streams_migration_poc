package com.test.portal.account.platform.kafka.consumer;

import com.test.portal.account.platform.kafka.config.ConsumerProperties;
import com.test.portal.account.platform.kafka.config.SourceType;
import com.test.portal.account.platform.util.DataConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ConsumerOperationsService {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  private final KafkaListenerEndpointRegistry registry;
  private final ConsumerProperties consumerProperties;
  private final KafkaConsumerConfiguration kafkaConsumerConfiguration;

  private Map<SourceType, String> listenerContainerByConsumerName;
  private Map<SourceType, Boolean> isEnabledMapByConsumerName;
  
  /**
   * Parameterized constructor.
   */
  public ConsumerOperationsService(KafkaListenerEndpointRegistry registry,
      ConsumerProperties consumerProperties, KafkaConsumerConfiguration kafkaConsumerConfiguration) {
    this.registry = registry;
    this.consumerProperties = consumerProperties;
    this.kafkaConsumerConfiguration = kafkaConsumerConfiguration;
  }

  /**
   * Initialize containerIsStoppedMap and listenerContainerByConsumerName.
   */
  @PostConstruct
  public void init() {
    populateIsEnabledMap();
    populateListenerContainerMap();
  }

  /**
   * starts consumer of given consumer name.
   *
   * @param sourceType sourceType
   */
  public void startConsumer(SourceType sourceType) {
    MessageListenerContainer messageListenerContainer =
        registry.getListenerContainer(listenerContainerByConsumerName.get(sourceType));
    if (isEnabledMapByConsumerName.get(sourceType) && Objects.nonNull(messageListenerContainer)
        && !messageListenerContainer.isRunning()) {
      messageListenerContainer.start();
    }
  }

  protected KafkaConsumer getKafkaConsumer(String topic) {
    Properties props = getConsumerProperties(topic);
    KafkaConsumer kafkaConsumer = new KafkaConsumer<String, String>(props);
    kafkaConsumer.subscribe(Arrays.asList(topic));
    return kafkaConsumer;
  }

  private Properties getConsumerProperties(String topic) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, getKafkaConsumerGroupId(topic));

    return props;
  }
  
  private String getKafkaConsumerGroupId(String topicName) {
    
    if (consumerProperties.getEnrichedIdentityTransactions().getTopics().contains(topicName)) {
      return consumerProperties.getEnrichedIdentityTransactions().getGroupId();
    }
    if (consumerProperties.getEnrichedTransactions().getTopics().contains(topicName)) {
      return consumerProperties.getEnrichedTransactions().getGroupId();
    }
    if (kafkaConsumerConfiguration.getIdentityTransactionSearchRetryStream().getTopic().contains(topicName)) {
      return kafkaConsumerConfiguration.getIdentityTransactionSearchRetryStream().getGroupId();
    }
    if (kafkaConsumerConfiguration.getTransactionSearchRetryStream().getTopic().contains(topicName)) {
      return kafkaConsumerConfiguration.getTransactionSearchRetryStream().getGroupId();
    }
    return "";
  }

  /**
   * this method populates the listenerContainerMap with consumerName key and listenerName as value.
   */
  private void populateListenerContainerMap() {
    listenerContainerByConsumerName = new EnumMap<>(SourceType.class);
    listenerContainerByConsumerName.put(SourceType.TRUCK, DataConstants.WAREHOUSE_LISTENER);
  }
  
  private void populateIsEnabledMap() {
    isEnabledMapByConsumerName = new EnumMap<>(SourceType.class);
    isEnabledMapByConsumerName.put(SourceType.TRUCK, consumerProperties.getEnrichedTransactions().isEnabled());
  }
}
