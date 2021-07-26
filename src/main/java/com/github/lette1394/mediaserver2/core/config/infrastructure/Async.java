package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class Async implements Reloader {
  private final Reloader reloader;
  private final ExecutorService executorService;

  @Override
  public CompletionStage<? super Void> reload() {
    return CompletableFuture.supplyAsync(reloader::reload, executorService);
  }
}
