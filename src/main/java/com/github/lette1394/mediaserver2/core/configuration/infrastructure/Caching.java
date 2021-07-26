package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


final class Caching implements FileResourceLoader {
  private final FileResourceLoader loader;
  private final Map<FileResource<?>, Try<?>> holder = new ConcurrentHashMap<>(4096);

  public Caching(FileResourceLoader fileResourceLoader) {
    this.loader = fileResourceLoader;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Try<T> load(FileResource<T> fileResource) {
    return (Try<T>) holder.computeIfAbsent(fileResource, loader::load);
  }
}
