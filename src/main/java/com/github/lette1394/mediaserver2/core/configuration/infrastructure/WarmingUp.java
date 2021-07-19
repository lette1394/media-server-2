package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import java.util.Set;

class WarmingUp implements UnsafeFileResources {
  private final UnsafeFileResources resources;

  public WarmingUp(
    UnsafeFileResources resources,
    ResourceScanner resourceScanner) throws IOException {

    this.resources = resources;
    loadAll(resourceScanner.scan());
  }

  private void loadAll(Set<? extends FileResource<?>> resourceSet) throws IOException {
    for (var fileResource : resourceSet) {
      resources.load(fileResource);
    }
  }

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException {
    return resources.load(fileResource);
  }
}

