package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MultiCache implements AllMultipleResources {
  private final AllMultipleResources resources;
  private final Map<Key, Option<?>> holder = new ConcurrentHashMap<>();

  public MultiCache(AllMultipleResources resources) {
    this.resources = resources;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Option<T> find(Class<T> type, String name) {
    final var key = new Key(type, name);
    if (holder.containsKey(key)) {
      return (Option<T>) holder.get(key);
    }

    final var result = resources.find(type, name);
    holder.put(key, result);
    return result;
  }

  private record Key(Class<?> type, String name) {
  }
}

