package com.test.portal.account.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.test.portal.account.platform"})
@RefreshScope
@Slf4j
public class Application {

  public static void main(String[] args)  {
    SpringApplication.run(Application.class, args);
  }
}
