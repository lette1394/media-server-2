package com.github.lette1394.mediaserver2.core.configuration.infrastructure.sample;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.JsonSchema;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.SingleFileResource;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonSchema(schemaFilePath = "/schema/store.json")
@SingleFileResource(filePath = "/store.yaml")
public class Store {
  List<String> fruits;
  List<Vegetable> vegetables;

  @Value
  @Builder
  @Jacksonized
  static class Vegetable {
    String veggieName;
    boolean veggieLike;
  }
}
