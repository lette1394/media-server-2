package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.vavr.control.Try;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class Warmer {
  private final ForkJoinPool forkJoinPool;
  private final Duration timeout;

  <T> Try<T> submit(Callable<T> callable) {
    return Try.of(() -> forkJoinPool
      .submit(callable)
      .get(timeout.toMillis(), MILLISECONDS));
  }

  @SuppressWarnings("unchecked")
  Try<Void> submit(Runnable runnable) {
    return (Try<Void>) Try.of(() -> forkJoinPool
      .submit(runnable)
      .get(timeout.toMillis(), MILLISECONDS));
  }
}
