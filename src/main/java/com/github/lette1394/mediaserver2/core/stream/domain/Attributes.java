package com.github.lette1394.mediaserver2.core.stream.domain;

import static com.github.lette1394.mediaserver2.core.stream.domain.Contracts.requires;

import io.vavr.control.Option;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public final class Attributes {
  private final Map<Attribute<?>, Object> holder;

  public static Attributes createEmpty() {
    return new Attributes(new ConcurrentHashMap<>());
  }

  @SuppressWarnings("unchecked")
  public <T> Option<T> get(Attribute<T> attribute) {
    return Option.of((T) holder.get(attribute));
  }

  public <T> void put(Attribute<T> attribute, T value) {
    requires(!holder.containsKey(attribute),
      "Attribute must be unique, duplicate: [%s, %s]".formatted(attribute, value));

    holder.put(attribute, value);
  }
}
