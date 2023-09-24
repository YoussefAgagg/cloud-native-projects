package com.github.youssefagagg.sample.controller;

import com.github.youssefagagg.sample.config.HelloProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequiredArgsConstructor
public class HomeController {
  private final HelloProperties helloProperties;

  @GetMapping("/")
  public String getGreeting() {
    return helloProperties.getGreeting();
  }
}
