package com.github.lette1394.mediaserver2.storage.persistence.domain;

import java.util.concurrent.CompletionStage;

public interface MetaChanges<T extends Meta> {
  void add(T entity);

  void update(T entity);

  void remove(T entity);

  CompletionStage<Void> flush();
}
