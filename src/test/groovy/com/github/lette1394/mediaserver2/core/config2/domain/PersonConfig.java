package com.github.lette1394.mediaserver2.core.config2.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@Config
@ConfigLocation("/persons")
@ConfigLocation("/person.yaml")
public class PersonConfig {
  String name;
  int age;
}
