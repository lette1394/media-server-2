package com.github.lette1394.mediaserver2.core.configuration.domain;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.SingleResource;

@SingleResource(filePath = "/animal.yaml")
public record Animal(Type type, String name) {
  enum Type {
    DOG, CAT
  }
}


