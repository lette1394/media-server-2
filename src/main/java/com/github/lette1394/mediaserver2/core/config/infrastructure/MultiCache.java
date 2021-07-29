package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class MultiCache implements AllMultiConfigs {
  private final AllMultiConfigs resources;
  private final Map<Key, Object> holder = new ConcurrentHashMap<>(4096);

  public MultiCache(AllMultiConfigs resources) {
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

