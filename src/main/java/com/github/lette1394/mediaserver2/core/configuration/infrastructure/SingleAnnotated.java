package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SingleAnnotated implements AllSingleResources {
  private final UnsafeFileResources unsafe;
  private final ClassPathFactory classPathFactory;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return Option
      .of(type.getAnnotation(SingleFileResource.class))
      .map(annotation -> new FileResource<>(type, classPathFactory.create(annotation)))
      .flatMap(this::load);
  }

  private <T> Option<T> load(FileResource<T> fileResource) {
    try {
      return Option.of(unsafe.load(fileResource));
    } catch (Exception e) {
      return Option.none();
    }
  }
}
