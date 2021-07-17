package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.storage.persistence.domain.Entity;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Flusher;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class StandardOutputFlusher<T extends Entity> implements Flusher<T> {
  @Override
  public CompletionStage<Void> flush(List<T> entities) {
    System.out.println(entities);
    return CompletableFuture.completedFuture(null);
  }
}
