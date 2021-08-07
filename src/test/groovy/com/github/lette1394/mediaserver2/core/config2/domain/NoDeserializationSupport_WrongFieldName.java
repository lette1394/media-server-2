package com.github.lette1394.mediaserver2.core.config2.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@Config
@ConfigLocation("/person.yaml")
class NoDeserializationSupport_WrongFieldName {
  String name;
  int ageeeeeeeeeeeeeeee; // wrong field name
}
