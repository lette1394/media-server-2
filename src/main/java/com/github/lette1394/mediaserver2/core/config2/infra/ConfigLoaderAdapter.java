package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ConfigLoaderAdapter implements AllConfigs {
  private final KeyFactory keyFactory;
  private final Loader loader;

  @Override
  public <T> T find(Class<T> deserializedType) {
    return loader.load(keyFactory.create(deserializedType));
  }

  @Override
  public <T> T find(Class<T> deserializedType, String name) {
    return loader.load(keyFactory.create(deserializedType, name));
  }
}
