package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigAlias;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
class Aliased implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;

  @Override
  public <T> FileKey<T> singleKey(Class<T> configType) {
    return findAlias(configType)
      .map(fileKeyFactory::singleKey)
      .getOrElse(() -> fileKeyFactory.singleKey(configType));
  }

  @Override
  public <T> MultiFileKey<T> multiKey(Class<T> configType) {
    return findAlias(configType)
      .map(fileKeyFactory::multiKey)
      .getOrElse(() -> fileKeyFactory.multiKey(configType));
  }

  @SuppressWarnings("unchecked")
  <T, R extends T> Option<Class<R>> findAlias(Class<T> configType) {
    final var collect = new Reflections("com.github.lette1394.mediaserver2")
      .getTypesAnnotatedWith(ConfigAlias.class, true)
      .stream()
      .filter(configType::isAssignableFrom)
      .collect(toUnmodifiableSet());

    if (collect.isEmpty()) {
      return Option.none();
    }
    requires(collect.size() == 1);
    return Option.of((Class<R>) collect.iterator().next());
  }
}
