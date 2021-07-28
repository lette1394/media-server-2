package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;

import com.github.lette1394.mediaserver2.core.config.domain.RawConfig;
import io.vavr.control.Option;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

@SuppressWarnings("rawtypes")
final class ScanningCached implements AllMappedResourceTypes {
  private final Map<Class<?>, ? extends Class<? extends RawConfig>> cached;

  public ScanningCached(Reflections reflections) {
    this.cached = scan(reflections);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T, R extends RawConfig<T>> Option<Class<R>> findMappedResource(Class<T> mappedType) {
    if (cached.containsKey(mappedType)) {
      return Option.of((Class<R>) cached.get(mappedType));
    }
    return Option.none();
  }

  private static Map<Class<?>, ? extends Class<? extends RawConfig>> scan(Reflections reflections) {
    return reflections
      .getSubTypesOf(RawConfig.class)
      .stream()
      .map(entityClassType -> {
        final Type[] genericInterfaces = entityClassType.getGenericInterfaces();
        requires(genericInterfaces.length > 0, "entityClass.genericInterfaces.length == 1");  // FIXME (jaeeun) 2021/07/25: description

        final Type genericInterface = genericInterfaces[0];
        requires(genericInterface instanceof ParameterizedType,
          "entityClass.genericInterface instanceof ParameterizedType"); // FIXME (jaeeun) 2021/07/25: description

        final ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        requires(actualTypeArguments.length == 1,
          "parameterizedType.actualTypeArguments.length == 1"); // FIXME (jaeeun) 2021/07/25: description

        final Type mappedType = actualTypeArguments[0];
        requires(mappedType instanceof Class, "mappedType instanceof Class"); // FIXME (jaeeun) 2021/07/25: description

        final Class<?> mappedClassType = (Class<?>)mappedType;
        return Pair.of(entityClassType, mappedClassType);
      })
      .collect(toMap(Pair::getRight, Pair::getLeft));
  }
}
