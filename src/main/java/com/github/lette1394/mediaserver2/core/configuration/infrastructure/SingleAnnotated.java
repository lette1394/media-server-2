package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SingleAnnotated implements AllSingleResources {
  private final FileResourceLoader loader;
  private final FileResourcePathFactory fileResourcePathFactory;

  @Override
  public <T> T find(Class<T> type) {
    return Option.of(type.getAnnotation(SingleFileResource.class)).toTry()
      .map(fileResourcePathFactory::create)
      .map(path -> new FileResource<>(type, path))
      .flatMap(loader::load)
      .get();
  }
}
