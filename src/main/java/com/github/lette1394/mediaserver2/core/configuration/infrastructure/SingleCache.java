package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SingleCache implements AllSingleResources {
  private final AllSingleResources resources;
  private final Map<Class<?>, Object> holder = new ConcurrentHashMap<>();

  public SingleCache(AllSingleResources resources) {
    this.resources = resources;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T find(Class<T> type) {
    return (T) holder.computeIfAbsent(type, resources::find);
  }
}

