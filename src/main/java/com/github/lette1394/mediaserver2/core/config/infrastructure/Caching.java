package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


final class Caching implements Deserializer {
  private final Deserializer deserializer;
  private final Map<FileConfig<?>, Try<?>> holder = new ConcurrentHashMap<>(4096);

  public Caching(Deserializer deSerializer) {
    this.deserializer = deSerializer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    return (Try<T>) holder.computeIfAbsent(fileConfig, deserializer::deserialize);
  }
}
