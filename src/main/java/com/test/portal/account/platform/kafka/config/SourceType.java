package com.test.portal.account.platform.kafka.config;

public enum SourceType {
  TRUCK("truck");

  private final String name;

  SourceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
