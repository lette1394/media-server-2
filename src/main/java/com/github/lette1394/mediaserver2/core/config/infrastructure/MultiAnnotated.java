package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiAnnotated implements AllMultiConfigs {
  private final Deserializer loader;
  private final ClassPathFileFactory classPathFileFactory;

  @Override
  public <T> T find(Class<T> type, String name) {
    return Option.of(type.getAnnotation(MultiFileResource.class)).toTry()
      .flatMap(annotation -> classPathFileFactory.create(annotation, name))
      .map(path -> new FileConfig<>(type, path))
      .flatMap(loader::deserialize)
      .get();
  }
}
