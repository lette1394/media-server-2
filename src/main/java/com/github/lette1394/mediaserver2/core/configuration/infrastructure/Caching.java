package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class Caching implements FileResourceLoader {
  private final Map<FileResource<?>, Try<?>> holder = new ConcurrentHashMap<>();
  private final FileResourceLoader loader;

  public Caching(FileResourceLoader fileResourceLoader) {
    this.loader = fileResourceLoader;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Try<T> load(FileResource<T> fileResource) {
    if (holder.containsKey(fileResource)) {
      return (Try<T>) holder.get(fileResource);
    }

    final Try<T> ret = loader.load(fileResource);
    holder.put(fileResource, ret);
    return ret;
  }
}
