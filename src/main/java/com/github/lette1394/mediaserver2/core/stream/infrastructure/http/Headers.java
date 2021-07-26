package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Headers {
  private final Map<String, String> holder;

  public Headers(Map<String, String> holder) {
    this.holder = holder;
  }

  public Optional<String> value(String name) {
    if (holder.containsKey(name)) {
      return Optional.of(holder.get(name));
    }
    return Optional.empty();
  }

  public Map<String, String> toMap() {
    return new HashMap<>(holder);
  }

  @Override
  public String toString() {
    return holder.toString();
  }
}
