package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.util.Set;

class WarmingUp implements FileResourceLoader {
  private final FileResourceLoader loader;

  private WarmingUp(FileResourceLoader loader) {
    this.loader = loader;
  }

  static Try<FileResourceLoader> create(
    FileResourceLoader loader,
    ResourceScanner resourceScanner) {

    return resourceScanner
      .scan()
      .flatMap(fileResources -> warmUp(loader, fileResources))
      .map(__ -> new WarmingUp(loader));
  }

  private static Try<Void> warmUp(
    FileResourceLoader loader,
    Set<? extends FileResource<?>> resourceSet) {

    return resourceSet
      .stream()
      .parallel()
      .map(loader::load)
      .reduce(Try.success(null),
        (t1, t2) -> t2.flatMap(__ -> t1),
        (t1, t2) -> t1.flatMap(__ -> t2));
  }

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    return loader.load(fileResource);
  }
}

