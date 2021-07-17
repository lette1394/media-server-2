package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MappedSingleResources implements AllSingleResources {
  private final AllSingleResources resources;
  private final AllEntityTypes allEntityTypes;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return allEntityTypes
      .relatedEntityType(type)
      .map(entity -> resources.find(entity).map(Entity::toMapped))
      .getOrElse(() -> resources.find(type));
  }
}
