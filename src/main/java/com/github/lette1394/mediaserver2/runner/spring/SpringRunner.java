package com.github.lette1394.mediaserver2.runner.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRunner {
  public static void main(String[] args) {
    SpringApplication.run(SpringRunner.class, args);
  }
}


// TODO:
//  1. try UnsafeFileResources
//  2. try scanner
//  3. multi thread loader (성능)
//  4. multi thread caching (버그)