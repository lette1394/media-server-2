package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FileExtensionAware implements UnsafeFileResources {
  private final FileExtension fileExtension;
  private final UnsafeFileResources delegate;

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException, URISyntaxException {
    return delegate.load(addYamlExtensionIfNotExist(fileResource));
  }

  private <T> FileResource<T> addYamlExtensionIfNotExist(FileResource<T> resource) {
    final var classPath = resource.classPath();
    final var lowerCaseExtension = fileExtension.name().toLowerCase();
    final var extension = ".%s".formatted(lowerCaseExtension);

    if (classPath.endsWith(extension)) {
      return resource;
    }
    return new FileResource<>(resource.type(), classPath.concat(extension));
  }

  enum FileExtension {
    YAML
  }
}
