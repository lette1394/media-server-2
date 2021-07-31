package com.github.lette1394.mediaserver2.core.config2.infra;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class Person {
  private final String name;
  private final int age;
}
