package com.github.youssefagagg.sample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
 
@Getter
@Setter
@ConfigurationProperties(prefix = "hello")
public class HelloProperties {
  /**
   * A message to welcome users.
   */
  private String greeting;
}