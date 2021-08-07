package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ConfigLoaderAdapter implements AllConfigs {
  private final FileKeyFactory fileKeyFactory;
  private final FileLoader fileLoader;

  @Override
  public <T> T find(Class<T> deserializedType) {
    return fileLoader.load(fileKeyFactory.create(deserializedType));
  }

  @Override
  public <T> T find(Class<T> deserializedType, String name) {
    return fileLoader.load(fileKeyFactory.create(deserializedType, name));
  }
}
