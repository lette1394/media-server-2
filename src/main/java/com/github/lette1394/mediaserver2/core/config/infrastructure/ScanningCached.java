package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static java.util.stream.Collectors.toMap;

import com.github.lette1394.mediaserver2.core.config.domain.TypeAlias;
import io.vavr.control.Option;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

final class ScanningCached implements AllRawConfigTypes {
  private final Map<Class<?>, Class<?>> cached;

  public ScanningCached(Reflections reflections) {
    this.cached = scan(reflections);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T, R extends T> Option<Class<R>> findRawConfig(Class<T> mappedType) {
    return Option.of((Class<R>) cached.get(mappedType));
  }

  private static Map<Class<?>, Class<?>> scan(Reflections reflections) {
    return reflections
      .getTypesAnnotatedWith(TypeAlias.class, true)
      .stream()
      .map(sourceType -> {
        final var targetType = sourceType.getAnnotation(TypeAlias.class).value();
        return Pair.of(targetType, sourceType);
      })
      .collect(toMap(Pair::getLeft, Pair::getRight));
  }
}
