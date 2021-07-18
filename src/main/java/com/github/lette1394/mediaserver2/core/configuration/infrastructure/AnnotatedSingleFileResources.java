package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AnnotatedSingleFileResources implements AllSingleResources {
  private final UnsafeFileResources unsafe;
  private final ClassPaths classPaths;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return Option
      .of(type.getAnnotation(SingleResource.class))
      .map(annotation -> fileResource(type, annotation))
      .flatMap(this::load);
  }

  private <T> FileResource<T> fileResource(Class<T> type, SingleResource annotation) {
    return new FileResource<>(type, classPaths.create(annotation.filePath()));
  }

  private <T> Option<T> load(FileResource<T> fileResource) {
    try {
      return Option.of(unsafe.load(fileResource));
    } catch (Exception e) {
      return Option.none();
    }
  }
}
