package com.github.lette1394.mediaserver2.core.domain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FluentCompletionStage {
  private FluentCompletionStage() {
  }

  public static CompletionStage<Void> start() {
    return CompletableFuture.completedFuture(null);
  }

  public static <T> Function<Throwable, ? extends T> peek(Runnable runnable) {
    runnable.run();

    return throwable -> {
      if (throwable instanceof CompletionException e) {
        throw e;
      }
      throw new CompletionException(throwable);
    };
  }

  public static <T> BiConsumer<? super T, ? super Throwable> consumeFinally(
    Consumer<Void> onFinally) {

    return (t, throwable) -> onFinally.accept(null);
  }
}
