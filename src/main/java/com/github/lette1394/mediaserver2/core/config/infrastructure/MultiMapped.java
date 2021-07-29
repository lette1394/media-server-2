package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MultiMapped implements AllMultiConfigs {
  private final AllMultiConfigs resources;
  private final AllTypeAliases allTypeAliases;

  @Override
  public <T> T find(Class<T> type, String name) {
    return allTypeAliases.findTypeAlias(type)
      .map(alias -> resources.find(alias, name))
      .getOrElse(() -> resources.find(type, name));
  }
}
