package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Set;
import java.util.concurrent.Callable;

final class WarmingUp implements Deserializer {
  private final Deserializer deserializer;

  private WarmingUp(Deserializer deserializer) {
    this.deserializer = deserializer;
  }

  static Try<Deserializer> createWarmingUp(
    Deserializer deserializer,
    ResourceScanner resourceScanner,
    Warmer warmer) {

    return resourceScanner
      .scan()
      .flatMap(fileResources -> warmUp(deserializer, fileResources, warmer))
      .map(__ -> new WarmingUp(deserializer));
  }

  private static Try<Void> warmUp(
    Deserializer deserializer,
    Set<? extends FileConfig<?>> resourceSet,
    Warmer warmer) {

    final Try<Void> initial = Try.success(null);
    final Callable<Try<Void>> task = () -> Try.of(() -> resourceSet
      .parallelStream()
      .map(deserializer::deserialize)
      .reduce(initial,
        (acc, t) -> t.flatMap(__ -> acc),
        (try1, try2) -> try1.flatMap(__ -> try2)))
      .flatMap(__ -> __);

    return warmer
      .submit(task)
      .flatMap(__ -> __);
  }

  @Override
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    return deserializer.deserialize(fileConfig);
  }
}

