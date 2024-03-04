package com.test.portal.account.platform.kafka.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConsumerSeekAware implements ConsumerSeekAware {
  
  private final ThreadLocal<ConsumerSeekCallback> callbackForThread = new ThreadLocal<>();
  
  private final Map<TopicPartition, ConsumerSeekCallback> callbacks = new ConcurrentHashMap<>();
  
  private final Map<ConsumerSeekCallback, List<TopicPartition>> callbacksToTopic = new ConcurrentHashMap<>();
  
  @Override
  public void registerSeekCallback(ConsumerSeekCallback callback) {
    this.callbackForThread.set(callback);
  }
  
  @Override
  public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
    ConsumerSeekCallback threadCallback = this.callbackForThread.get();
    if (threadCallback != null) {
      assignments.keySet().forEach(tp -> {
        this.callbacks.put(tp, threadCallback);
        this.callbacksToTopic.computeIfAbsent(threadCallback, key -> new LinkedList<>()).add(tp);
      });
    }
  }
  
  @Override
  public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
  }
}
