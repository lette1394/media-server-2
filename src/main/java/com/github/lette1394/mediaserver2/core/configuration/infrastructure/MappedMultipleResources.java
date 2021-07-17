package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MappedMultipleResources implements AllMultipleResources {
  private final AllMultipleResources resources;
  private final AllEntityTypes allEntityTypes;

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return allEntityTypes
      .relatedEntityType(type)
      .map(entity -> resources.find(entity, name).map(Entity::toMapped))
      .getOrElse(() -> resources.find(type, name));
  }
}
