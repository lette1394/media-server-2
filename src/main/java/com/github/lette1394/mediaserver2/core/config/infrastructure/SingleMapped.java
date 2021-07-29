package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SingleMapped implements AllSingleConfigs {
  private final AllSingleConfigs resources;
  private final AllRawConfigTypes allRawConfigTypes;

  @Override
  public <T> T find(Class<T> type) {
    return allRawConfigTypes.findRawConfig(type)
      .map(resources::find)
      .getOrElse(() -> resources.find(type));
  }
}
