package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

class SingleReloading implements Reloader, AllSingleResources {
  private final AtomicReference<AllSingleResources> ref;
  private final Supplier<Try<AllSingleResources>> supplier;

  public SingleReloading(
    Supplier<Try<AllSingleResources>> supplier,
    AllSingleResources allSingleResources) {

    this.supplier = supplier;
    this.ref = new AtomicReference<>(allSingleResources);
  }

  @Override
  public Try<Void> reload() {
    return supplier
      .get()
      .peek(ref::set)
      .map(__ -> null);
  }

  @Override
  public <T> Option<T> find(Class<T> type) {
    return ref.get().find(type);
  }
}

