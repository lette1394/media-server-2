package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiAnnotated implements AllMultipleResources {
  private final FileResourceLoader loader;
  private final FileResourcePathFactory fileResourcePathFactory;

  @Override
  public <T> T find(Class<T> type, String name) {
    return Option.of(type.getAnnotation(MultiFileResource.class)).toTry()
      .flatMap(annotation -> fileResourcePathFactory.create(annotation, name))
      .map(path -> new FileResource<>(type, path))
      .flatMap(loader::load)
      .get();
  }
}
