package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class BaseClassPathAware implements UnsafeFileResources {
  private final ClassPath baseClassPath;
  private final UnsafeFileResources delegate;

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException, URISyntaxException {
    return delegate.load(addBaseClassPath(fileResource));
  }

  private <T> FileResource<T> addBaseClassPath(FileResource<T> fileResource) {
    final var classPath = baseClassPath.concat(fileResource.classPath());
    return new FileResource<>(fileResource.type(), classPath);
  }
}
