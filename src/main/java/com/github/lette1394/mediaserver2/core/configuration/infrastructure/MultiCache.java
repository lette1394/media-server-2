package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MultiCache implements AllMultipleResources {
  private final AllMultipleResources resources;
  private final Map<Key, Object> holder = new ConcurrentHashMap<>(4096);

  public MultiCache(AllMultipleResources resources) {
    this.resources = resources;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T find(Class<T> type, String name) {
    return (T) holder.computeIfAbsent(new Key(type, name), __ -> resources.find(type, name));
  }

  private record Key(Class<?> type, String name) {
  }
}

