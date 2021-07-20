package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MultiAnnotated implements AllMultipleResources {
  private final FileResourceLoader loader;
  private final FileResourcePathFactory fileResourcePathFactory;

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return Option
      .of(type.getAnnotation(MultiFileResource.class))
      .map(annotation -> new FileResource<>(type, fileResourcePathFactory.create(annotation, name)))
      .flatMap(this::load);
  }

  private <T> Option<T> load(FileResource<T> fileResource) {
    return loader.load(fileResource).toOption();
  }
}
