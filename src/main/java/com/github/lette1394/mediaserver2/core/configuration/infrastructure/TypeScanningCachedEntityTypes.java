package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.vavr.control.Option;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

@SuppressWarnings("rawtypes")
class TypeScanningCachedEntityTypes implements AllEntityTypes {
  private final Map<Class<?>, ? extends Class<? extends Entity>> cached;

  public TypeScanningCachedEntityTypes(String basePackage) {
    requires(isNotBlank(basePackage), "isNotBlank(basePackage)");
    this.cached = scan(basePackage);
  }

  @SuppressWarnings("unchecked")
  public <T, R extends Entity<T>> Option<Class<R>> relatedEntityType(Class<T> type) {
    if (cached.containsKey(type)) {
      return Option.of((Class<R>) cached.get(type));
    }
    return Option.none();
  }

  private static Map<Class<?>, ? extends Class<? extends Entity>> scan(String basePackage) {
    return new Reflections(basePackage, new SubTypesScanner())
      .getSubTypesOf(Entity.class)
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
