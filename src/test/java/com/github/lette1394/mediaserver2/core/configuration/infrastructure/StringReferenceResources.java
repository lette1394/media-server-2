package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;

public class StringReferenceResources implements AllSingleResources {
  private final ObjectMapper objectMapper;
  private AtomicReference<String> textReference;

  public StringReferenceResources(ObjectMapper objectMapper, AtomicReference<String> textReference) {
    this.objectMapper = objectMapper;
    this.textReference = textReference;
  }

  @Override
  @SneakyThrows
  public <T> T find(Class<T> type) {
    return objectMapper.readValue(textReference.get(), type);
  }
}
