package com.github.lette1394.mediaserver2.core.config2.infra;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
class Person {
  private final String name;
  private final int age;
}
