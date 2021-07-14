package com.github.lette1394.mediaserver2.storage.lock.domain;

import java.util.concurrent.CompletionStage;

public interface Locker {
  CompletionStage<Void> lock();

  CompletionStage<Void> unlock();
}
