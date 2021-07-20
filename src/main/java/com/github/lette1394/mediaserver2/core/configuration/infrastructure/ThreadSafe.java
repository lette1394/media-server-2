package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ThreadSafe implements FileResourceLoader {
  private final FileResourceLoader loader;
  private final Map<FileResource<?>, Object> locks = new ConcurrentHashMap<>();

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException {
    synchronized (locks.computeIfAbsent(fileResource, __ -> new Object())) {
      return loader.load(fileResource);
    }
  }
}
