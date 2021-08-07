package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import java.util.Map;
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
    final var multiKey = fileKeyFactory.multiKey(deserializedType);
    requires(multiKey.containsKey(name), () -> new ConfigException("""
      unknown config name: [%s]""".formatted(name)));
    return fileLoader.load(multiKey.get(name));
  }
}
