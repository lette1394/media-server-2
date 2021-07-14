package com.github.lette1394.mediaserver2.storage.domain;

import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class NoOpLocker implements Locker {
  @Override
  public CompletionStage<Void> lock() {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletionStage<Void> unlock() {
    return CompletableFuture.completedFuture(null);
  }
}
