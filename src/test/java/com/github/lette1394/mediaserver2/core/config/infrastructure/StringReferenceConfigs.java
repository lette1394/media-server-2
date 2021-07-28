package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;

public class StringReferenceConfigs implements AllSingleConfigs {
  private final ObjectMapper objectMapper;
  private AtomicReference<String> textReference;

  public StringReferenceConfigs(ObjectMapper objectMapper, AtomicReference<String> textReference) {
    this.objectMapper = objectMapper;
    this.textReference = textReference;
  }

  @Override
  @SneakyThrows
  public <T> T find(Class<T> type) {
    return objectMapper.readValue(textReference.get(), type);
  }
}
