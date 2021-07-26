package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.util.Set;
import java.util.concurrent.Callable;

final class WarmingUp implements FileResourceLoader {
  private final FileResourceLoader loader;

  private WarmingUp(FileResourceLoader loader) {
    this.loader = loader;
  }

  static Try<FileResourceLoader> create(
    FileResourceLoader loader,
    ResourceScanner resourceScanner,
    Warmer warmer) {

    return resourceScanner
      .scan()
      .flatMap(fileResources -> warmUp(loader, fileResources, warmer))
      .map(__ -> new WarmingUp(loader));
  }

  private static Try<Void> warmUp(
    FileResourceLoader loader,
    Set<? extends FileResource<?>> resourceSet,
    Warmer warmer) {

    final Try<Void> initial = Try.success(null);
    final Callable<Try<Void>> task = () -> Try.of(() -> resourceSet
      .parallelStream()
      .map(loader::load)
      .reduce(initial,
        (acc, t) -> t.flatMap(__ -> acc),
        (t1, t2) -> t1.flatMap(__ -> t2)))
      .flatMap(__ -> __);

    return warmer
      .submit(task)
      .flatMap(__ -> __);
  }

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    return loader.load(fileResource);
  }
}

