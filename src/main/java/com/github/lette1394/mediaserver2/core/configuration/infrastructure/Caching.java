package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class Caching implements FileResourceLoader {
  private final Map<FileResource<?>, Object> holder = new ConcurrentHashMap<>();
  private final FileResourceLoader loader;

  public Caching(FileResourceLoader fileResourceLoader) {
    this.loader = fileResourceLoader;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T load(FileResource<T> fileResource) throws IOException {
    if (holder.containsKey(fileResource)) {
      return (T) holder.get(fileResource);
    }

    final T ret = loader.load(fileResource);
    holder.put(fileResource, ret);
    return ret;
  }
}
