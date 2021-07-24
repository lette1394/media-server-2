package com.github.lette1394.mediaserver2.core.domain;

import io.vavr.control.Option;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Attributes {
  private final Map<Attribute<?>, Object> holder;

  public static Attributes empty() {
    return new Attributes(new HashMap<>());
  }

  @SuppressWarnings("unchecked")
  public <T> Option<T> getAttribute(Attribute<T> attribute) {
    if (holder.containsKey(attribute)) {
      return Option.of((T) holder.get(attribute));
    }
    return Option.none();
  }

  public <T> void putAttribute(Attribute<T> attribute, T value) {
    holder.put(attribute, value);
  }
}
