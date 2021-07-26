package com.github.lette1394.mediaserver2.storage.persistence.domain;

import java.util.List;
import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Flusher<T extends Meta> {
  CompletionStage<Void> flush(List<T> entities);
}
