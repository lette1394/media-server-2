package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import io.vavr.control.Try;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

final class SingleReloading implements Reloader, AllSingleConfigs {
  private final AtomicReference<AllSingleConfigs> ref;
  private final Supplier<Try<AllSingleConfigs>> supplier;

  public SingleReloading(
    Supplier<Try<AllSingleConfigs>> supplier,
    AllSingleConfigs allSingleConfigs) {

    this.supplier = supplier;
    this.ref = new AtomicReference<>(allSingleConfigs);
  }

  @Override
  public CompletionStage<? super Void> reload() {
    return supplier
      .get()
      .peek(ref::set)
      .map(__ -> null)
      .toCompletableFuture();
  }

  @Override
  public <T> T find(Class<T> type) {
    return ref.get().find(type);
  }
}

