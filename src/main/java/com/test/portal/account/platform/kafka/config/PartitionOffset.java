package com.test.portal.account.platform.kafka.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartitionOffset {
  private Long producerOffset;
  private Long committedOffset;
}

