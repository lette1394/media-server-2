package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleResources;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiMapped implements AllMultipleResources {
  private final AllMultipleResources resources;
  private final AllMappedResourceTypes allMappedResourceTypes;

  @Override
  public <T> T find(Class<T> type, String name) {
    return allMappedResourceTypes
      .findMappedResource(type)
      .map(entity -> resources.find(entity, name).toMapped())
      .getOrElse(() -> resources.find(type, name));
  }
}
