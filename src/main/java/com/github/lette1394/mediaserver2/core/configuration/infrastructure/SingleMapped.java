package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SingleMapped implements AllSingleResources {
  private final AllSingleResources resources;
  private final AllMappedResourceTypes allMappedResourceTypes;

  @Override
  public <T> T find(Class<T> type) {
    return allMappedResourceTypes
      .findMappedResource(type)
      .map(entity -> resources.find(entity).toMapped())
      .getOrElse(() -> resources.find(type));
  }
}
