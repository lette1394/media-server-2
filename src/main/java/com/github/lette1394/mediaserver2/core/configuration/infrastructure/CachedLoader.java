package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


class CachedLoader implements UnsafeFileResources {
  private final Map<FileResource<?>, Object> holder = new ConcurrentHashMap<>();
  private final AtomicReference<UnsafeFileResources> resourcesRef;

  public CachedLoader(UnsafeFileResources unsafeFileResources) {
    this.resourcesRef = new AtomicReference<>(unsafeFileResources);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T load(FileResource<T> fileResource) throws IOException, URISyntaxException {
    if (holder.containsKey(fileResource)) {
      return (T) holder.get(fileResource);
    }

    final T ret = resourcesRef.get().load(fileResource);
    holder.put(fileResource, ret);
    return ret;
  }

  public void updateWith() {
    holder.clear();
  }
}
