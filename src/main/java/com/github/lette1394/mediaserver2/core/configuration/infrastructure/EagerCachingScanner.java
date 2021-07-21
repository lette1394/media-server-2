package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

class EagerCachingScanner implements ResourceScanner {
  private final AtomicReference<Set<? extends FileResource<?>>> cacheRef;

  private EagerCachingScanner(AtomicReference<Set<? extends FileResource<?>>> cacheRef) {
    this.cacheRef = cacheRef;
  }

  static Try<ResourceScanner> create(ResourceScanner resourceScanner) {
    return resourceScanner.scan()
      .map(fileResources -> new EagerCachingScanner(new AtomicReference<>(fileResources)));
  }

  @Override
  public Try<Set<? extends FileResource<?>>> scan() {
    return Try.success(cacheRef.get());
  }
}
