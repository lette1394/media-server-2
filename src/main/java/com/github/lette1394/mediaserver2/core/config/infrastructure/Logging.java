package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class Logging implements Deserializer {
  private final Deserializer deserializer;

  @Override
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    return deserializer.deserialize(fileConfig)
      .onSuccess(__ -> log.info("loaded: [{}]", fileConfig))
      .onFailure(throwable -> log.error("got exception during loading: [{}], cause: [{}]",
        fileConfig, throwable));
  }
}
