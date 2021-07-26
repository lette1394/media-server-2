package com.github.lette1394.mediaserver2.storage.lock.infrastructure;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedLocker implements Locker {
  private final Locker locker;
  private final Trace trace;

  @Override
  public CompletionStage<Void> lock() {
    log.info("{} on locking", trace);
    final var result = locker.lock();
    log.info("{} on locked", trace);

    return result;
  }

  @Override
  public CompletionStage<Void> unlock() {
    log.info("{} on unlocking", trace);
    final var result = locker.lock();
    log.info("{} on unlocked", trace);

    return result;
  }
}
