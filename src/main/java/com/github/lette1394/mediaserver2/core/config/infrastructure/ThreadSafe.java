package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class ThreadSafe implements FileResourceLoader {
  private final FileResourceLoader loader;
  private final Map<FileResource<?>, Object> locks = new ConcurrentHashMap<>(4096);

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    synchronized (locks.computeIfAbsent(fileResource, __ -> new Object())) {
      return loader.load(fileResource);
    }
  }
}
