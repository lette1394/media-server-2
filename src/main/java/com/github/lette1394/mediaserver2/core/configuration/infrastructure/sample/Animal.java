package com.github.lette1394.mediaserver2.core.configuration.infrastructure.sample;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.SingleFileResource;

@SingleFileResource(filePath = "/animal.yaml")
public record Animal(Type type, String name) {
  enum Type {
    DOG, CAT
  }
}
