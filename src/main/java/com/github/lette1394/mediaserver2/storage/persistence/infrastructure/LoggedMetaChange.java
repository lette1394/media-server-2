package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.peek;
import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedMetaChange<T> implements MetaChange<T> {
  private final MetaChange<T> metaChange;
  private final Trace trace;

  @Override
  public void add(T entity) {
    metaChange.add(entity);
    log.info("{}> registered to add: {}", trace, entity.toString());
  }

  @Override
  public void update(T entity) {
    metaChange.update(entity);
    log.info("{}> registered to update: {}", trace, entity.toString());
  }

  @Override
  public void remove(T entity) {
    metaChange.remove(entity);
    log.info("{}> registered to remove: {}", trace, entity.toString());
  }

  @Override
  public CompletionStage<Void> flush() {
    log.info("{}> flushing ...", trace);
    return start()
      .thenCompose(__ -> metaChange.flush())
      .thenRun(() -> log.info("{}> flushed successfully", trace))
      .exceptionally(peek(() -> log.info("{}> flushed successfully", trace)));
  }
}
