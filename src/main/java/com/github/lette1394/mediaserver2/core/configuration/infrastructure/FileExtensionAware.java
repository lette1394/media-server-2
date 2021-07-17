package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.lang.String.format;

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
    final var rawPath = resource.classPath().rawPath();
    final var lowerCaseExtension = fileExtension.name().toLowerCase();
    if (rawPath.endsWith(format(".%s", lowerCaseExtension))) {
      return resource;
    }
    final var type = resource.type();
    final var classPath = new ClassPath(format("%s.%s", rawPath, lowerCaseExtension));
    return new FileResource<>(type, classPath);
  }

  enum FileExtension {
    YAML
  }
}
