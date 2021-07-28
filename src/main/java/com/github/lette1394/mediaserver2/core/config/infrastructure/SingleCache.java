package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class SingleCache implements AllSingleConfigs {
  private final AllSingleConfigs resources;
  private final Map<Class<?>, Object> holder = new ConcurrentHashMap<>(4096);

  public SingleCache(AllSingleConfigs resources) {
    this.resources = resources;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T find(Class<T> type) {
    return (T) holder.computeIfAbsent(type, resources::find);
  }
}

