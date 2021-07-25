package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;

import com.github.lette1394.mediaserver2.core.configuration.domain.MappedResource;
import io.vavr.control.Option;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

@SuppressWarnings("rawtypes")
class ScanningCached implements AllMappedResourceTypes {
  private final Map<Class<?>, ? extends Class<? extends MappedResource>> cached;

  public ScanningCached(Reflections reflections) {
    this.cached = scan(reflections);
  }

  @SuppressWarnings("unchecked")
  public <T, R extends MappedResource<T>> Option<Class<R>> findMappedResource(Class<T> mappedType) {
    if (cached.containsKey(mappedType)) {
      return Option.of((Class<R>) cached.get(mappedType));
    }
    return Option.none();
  }

  private static Map<Class<?>, ? extends Class<? extends MappedResource>> scan(Reflections reflections) {
    return reflections
      .getSubTypesOf(MappedResource.class)
      .stream()
      .map(entityClassType -> {
        final Type[] genericInterfaces = entityClassType.getGenericInterfaces();
        requires(genericInterfaces.length == 1, "entityClass.genericInterfaces.length == 1");

        final Type genericInterface = genericInterfaces[0];
        requires(genericInterface instanceof ParameterizedType,
          "entityClass.genericInterface instanceof ParameterizedType");

        final ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        requires(actualTypeArguments.length == 1,
          "parameterizedType.actualTypeArguments.length == 1");

        final Type mappedType = actualTypeArguments[0];
        requires(mappedType instanceof Class, "mappedType instanceof Class");

        final Class<?> mappedClassType = (Class<?>)mappedType;
        return Pair.of(entityClassType, mappedClassType);
      })
      .collect(toMap(Pair::getRight, Pair::getLeft));
  }
}
