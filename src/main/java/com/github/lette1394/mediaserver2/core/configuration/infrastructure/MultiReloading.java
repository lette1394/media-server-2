package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

class MultiReloading implements Reloader, AllMultipleResources {
  private final AtomicReference<AllMultipleResources> ref;
  private final Supplier<Try<AllMultipleResources>> supplier;

  public MultiReloading(
    Supplier<Try<AllMultipleResources>> supplier,
    AllMultipleResources allMultipleResources) {

    this.supplier = supplier;
    this.ref = new AtomicReference<>(allMultipleResources);
  }

  @Override
  public Try<Void> reload() {
    return supplier
      .get()
      .peek(ref::set)
      .map(__ -> null);
  }

  @Override
  public <T> T find(Class<T> type, String name) {
    return ref.get().find(type, name);
  }
}

