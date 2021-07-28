package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigs;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class MultiCache implements AllMultipleConfigs {
  private final AllMultipleConfigs resources;
  private final Map<Key, Object> holder = new ConcurrentHashMap<>(4096);

  public MultiCache(AllMultipleConfigs resources) {
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

