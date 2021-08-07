package com.github.lette1394.mediaserver2.core.config2.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@Config
@ConfigLocation("/core/config2/animals/dogs")
@ConfigLocation("/core/config2/animals/cat.yaml")
public class AnimalConfig {
  Type type;
  int age;

  enum Type {
    DOG, CAT
  }
}
