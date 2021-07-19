package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

class EagerCachingScanner implements ResourceScanner {
  private final AtomicReference<Set<? extends FileResource<?>>> cacheRef;

  public EagerCachingScanner(ResourceScanner resourceScanner) {
    cacheRef = new AtomicReference<>(resourceScanner.scan());
  }

  @Override
  public Set<? extends FileResource<?>> scan() {
    return cacheRef.get();
  }
}
