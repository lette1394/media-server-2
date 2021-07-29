package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class Jackson implements Deserializer {
  private final ObjectMapper objectMapper;

  @Override
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    return Try.of(() -> objectMapper.readValue(
      fileConfig.contents(),
      fileConfig.deserializedType()));
  }
}
