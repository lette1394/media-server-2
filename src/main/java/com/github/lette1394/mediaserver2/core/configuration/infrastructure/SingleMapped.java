package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SingleMapped implements AllSingleResources {
  private final AllSingleResources resources;
  private final AllMappedResourceTypes allMappedResourceTypes;

  @Override
  public <T> Option<T> find(Class<T> type) {
    return allMappedResourceTypes
      .findMappedResource(type)
      .map(entity -> resources.find(entity).map(MappedResource::toMapped))
      .getOrElse(() -> resources.find(type));
  }
}
