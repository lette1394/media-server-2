package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ConfigLoaderAdapter implements AllConfigs {
  private final FileKeyFactory fileKeyFactory;
  private final FileLoader fileLoader;

  @Override
  public <T> T find(Class<T> deserializedType) {
    return fileLoader.load(fileKeyFactory.singleKey(deserializedType));
  }

  @Override
  public <T> T find(Class<T> deserializedType, String name) {
    return fileKeyFactory.multiKey(deserializedType)
      .belongsTo(name)
      .map(fileLoader::load)
      .getOrElseThrow(() -> new ConfigException("Unknown config name: [%s]".formatted(name)));
  }
}
