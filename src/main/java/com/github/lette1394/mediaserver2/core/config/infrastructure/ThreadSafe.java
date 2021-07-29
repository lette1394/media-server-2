package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class ThreadSafe implements Deserializer {
  private final Deserializer loader;
  private final Map<FileConfig<?>, Object> locks = new ConcurrentHashMap<>(4096);

  @Override
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    synchronized (locks.computeIfAbsent(fileConfig, __ -> new Object())) {
      return loader.deserialize(fileConfig);
    }
  }
}
