package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigs;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiMapped implements AllMultipleConfigs {
  private final AllMultipleConfigs resources;
  private final AllRawConfigTypes allRawConfigTypes;

  @Override
  public <T> T find(Class<T> type, String name) {
    return allRawConfigTypes.findRawConfig(type)
      .map(entity -> resources.find(entity, name))
      .getOrElse(() -> resources.find(type, name));
  }
}
