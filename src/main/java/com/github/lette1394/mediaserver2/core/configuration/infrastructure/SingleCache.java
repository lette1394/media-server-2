package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SingleCache implements AllSingleResources {
  private final AllSingleResources resources;
  private final Map<Class<?>, Option<?>> holder = new ConcurrentHashMap<>();

  public SingleCache(AllSingleResources resources) {
    this.resources = resources;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Option<T> find(Class<T> type) {
    if (holder.containsKey(type)) {
      return (Option<T>) holder.get(type);
    }

    final var result = resources.find(type);
    holder.put(type, result);
    return result;
  }
}

