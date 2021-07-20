package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

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
  public <T> T load(FileResource<T> fileResource) throws IOException {
    return loader.load(fileResource);
  }
}

