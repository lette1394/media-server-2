package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.io.IOException;
import java.util.Set;

class WarmingUp implements FileResourceLoader {
  private final FileResourceLoader loader;

  public WarmingUp(
    FileResourceLoader loader,
    ResourceScanner resourceScanner) throws IOException {

    this.loader = loader;
    loadAll(resourceScanner.scan());
  }

  private void loadAll(Set<? extends FileResource<?>> resourceSet) throws IOException {
    for (var fileResource : resourceSet) {
      loader.load(fileResource);
    }
  }

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    return loader.load(fileResource);
  }
}

