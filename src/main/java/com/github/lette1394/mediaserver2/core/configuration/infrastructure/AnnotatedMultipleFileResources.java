package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AnnotatedMultipleFileResources implements AllMultipleResources {
  private final UnsafeFileResources unsafe;
  private final ClassPathFactory classPathFactory;

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return Option
      .of(type.getAnnotation(MultipleResource.class))
      .map(annotation -> fileResource(type, annotation, name))
      .flatMap(this::load);
  }

  private <T> FileResource<T> fileResource(
    Class<T> type,
    MultipleResource annotation,
    String name) {

    final var directoryPath = annotation.directoryPath();
    requires(isNotBlank(directoryPath), "isNotBlank(directoryPath)");
    final var classPath = classPathFactory.create("%s/%s".formatted(directoryPath, name));
    return new FileResource<>(type, classPath);
  }

  private <T> Option<T> load(FileResource<T> fileResource) {
    try {
      return Option.of(unsafe.load(fileResource));
    } catch (Exception e) {
      return Option.none();
    }
  }
}
