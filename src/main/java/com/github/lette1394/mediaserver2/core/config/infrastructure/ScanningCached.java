package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;

import com.github.lette1394.mediaserver2.core.config.domain.TypeAlias;
import io.vavr.control.Option;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

final class ScanningCached implements AllTypeAliases {
  private final Map<Class<?>, Class<?>> cached;

  public ScanningCached(Reflections reflections) {
    this.cached = scan(reflections);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T, R extends T> Option<Class<R>> findTypeAlias(Class<T> sourceType) {
    return Option.of((Class<R>) cached.get(sourceType));
  }

  private static Map<Class<?>, Class<?>> scan(Reflections reflections) {
    return reflections
      .getTypesAnnotatedWith(TypeAlias.class, true)
      .stream()
      .map(sourceType -> {
        final var targetType = sourceType.getAnnotation(TypeAlias.class).value();
        requires(targetType.isAssignableFrom(sourceType), "[%s] should be subclass of [%s]".formatted(sourceType, targetType));
        return Pair.of(targetType, sourceType);
      })
      .collect(toMap(Pair::getLeft, Pair::getRight));
  }
}
