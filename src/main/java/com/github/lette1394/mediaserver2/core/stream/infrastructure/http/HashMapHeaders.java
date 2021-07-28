package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import io.vavr.control.Option;
import java.util.HashMap;
import java.util.Map;

public final class HashMapHeaders implements Headers {
  private final Map<String, String> holder;

  public HashMapHeaders(Map<String, String> holder) {
    this.holder = new HashMap<>(holder);
  }

  @Override
  public Option<String> value(String name) {
    return Option.of(name);
  }

  @Override
  public Map<String, String> toMap() {
    return new HashMap<>(holder);
  }

  @Override
  public String toString() {
    return holder.toString();
  }
}
