package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.FluentCompletionStage.peek;
import static com.github.lette1394.mediaserver2.core.fluency.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChanges;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedMetaChanges<T extends Meta> implements MetaChanges<T> {
  private final MetaChanges<T> metaChanges;
  private final Trace trace;

  @Override
  public void add(T entity) {
    metaChanges.add(entity);
    log.info("{}> registered to add: {}", trace, entity.toString());
  }

  @Override
  public void update(T entity) {
    metaChanges.update(entity);
    log.info("{}> registered to update: {}", trace, entity.toString());
  }

  @Override
  public void remove(T entity) {
    metaChanges.remove(entity);
    log.info("{}> registered to remove: {}", trace, entity.toString());
  }

  @Override
  public CompletionStage<Void> flush() {
    log.info("{}> flushing ...", trace);
    return start()
      .thenCompose(__ -> metaChanges.flush())
      .thenRun(() -> log.info("{}> flushed successfully", trace))
      .exceptionally(peek(e -> log.info("{}> exception occurred: [%s]".formatted(e), trace)));
  }
}
