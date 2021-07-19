package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MultiAnnotated implements AllMultipleResources {
  private final UnsafeFileResources unsafe;
  private final ClassPathFactory classPathFactory;

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return Option
      .of(type.getAnnotation(MultiFileResource.class))
      .map(annotation -> fileResource(type, annotation, name))
      .flatMap(this::load);
  }

  private <T> FileResource<T> fileResource(
    Class<T> type,
    MultiFileResource annotation,
    String name) {

    final var path = annotation.directoryPath();
    requires(isNotBlank(path), "isNotBlank(path)");
    final var directory = classPathFactory.create(path);
    final var file = directory.concat("/%s".formatted(name));
    return new FileResource<>(type, file);
  }

  private <T> Option<T> load(FileResource<T> fileResource) {
    try {
      return Option.of(unsafe.load(fileResource));
    } catch (Exception e) {
      return Option.none();
    }
  }
}
