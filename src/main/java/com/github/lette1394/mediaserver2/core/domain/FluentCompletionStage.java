package com.github.lette1394.mediaserver2.core.domain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class FluentCompletionStage {
  private FluentCompletionStage() {
  }

  public static CompletionStage<Void> start() {
    return CompletableFuture.completedFuture(null);
  }

  public static <T> BiConsumer<? super T, ? super Throwable> consumeFinally(
    Consumer<Void> onFinally) {

    return (t, throwable) -> onFinally.accept(null);
  }
}
