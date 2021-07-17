package com.github.lette1394.mediaserver2.storage.lock.infrastructure;

import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
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
