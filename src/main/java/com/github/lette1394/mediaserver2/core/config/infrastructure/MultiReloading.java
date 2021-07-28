package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import io.vavr.control.Try;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

final class MultiReloading implements Reloader, AllMultipleConfigs {
  private final AtomicReference<AllMultipleConfigs> ref;
  private final Supplier<Try<AllMultipleConfigs>> supplier;

  public MultiReloading(
    Supplier<Try<AllMultipleConfigs>> supplier,
    AllMultipleConfigs allMultipleConfigs) {

    this.supplier = supplier;
    this.ref = new AtomicReference<>(allMultipleConfigs);
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
  public <T> T find(Class<T> type, String name) {
    return ref.get().find(type, name);
  }
}

