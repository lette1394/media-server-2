package com.github.lette1394.mediaserver2.storage.container.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.lette1394.mediaserver2.storage.persistence.domain.Entity;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Flusher;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Meta;
import java.util.List;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExhaustiveMetaFlusher<T extends Entity> implements Flusher<T> {
  private final Flusher<ExhaustiveMeta> flusher;

  @Override
  public CompletionStage<Void> flush(List<T> entities) {
    final var groupById = entities
      .stream()
      .collect(groupingBy(Entity::id));

    final var list = groupById
      .entrySet()
      .stream()
      .map(e -> new ExhaustiveMeta(
        e.getKey(),
        cast(e.getValue(), Meta.class),
        cast(e.getValue(), com.github.lette1394.mediaserver2.media.image.domain.Meta.class),
        cast(e.getValue(), com.github.lette1394.mediaserver2.media.video.domain.Meta.class)
      ))
      .collect(toList());

    return flusher.flush(list);
  }

  @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
  private <T extends Entity, R extends Entity> R cast(List<T> entities, Class<R> metaClass) {
    return entities
      .stream()
      .filter(metaClass::isInstance)
      .map(entity -> (R) entity)
      .findAny()
      .orElse(null); // FIXME (jaeeun) 2021/07/17: naive impl
  }
}
