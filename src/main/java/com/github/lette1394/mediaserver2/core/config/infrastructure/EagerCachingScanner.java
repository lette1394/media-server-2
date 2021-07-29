package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

final class EagerCachingScanner implements ResourceScanner {
  private final AtomicReference<Set<? extends FileConfig<?>>> cacheRef;

  private EagerCachingScanner(AtomicReference<Set<? extends FileConfig<?>>> cacheRef) {
    this.cacheRef = cacheRef;
  }

  static Try<ResourceScanner> createEagerCachingScanner(ResourceScanner resourceScanner) {
    return resourceScanner.scan()
      .map(fileResources -> new EagerCachingScanner(new AtomicReference<>(fileResources)));
  }

  @Override
  public Try<Set<? extends FileConfig<?>>> scan() {
    return Try.success(cacheRef.get());
  }
}
