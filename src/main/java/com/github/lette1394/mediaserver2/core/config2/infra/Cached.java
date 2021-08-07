package com.github.lette1394.mediaserver2.core.config2.infra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Cached implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;
  private final Map<Class<?>, FileKey<?>> singleKeyCache = new ConcurrentHashMap<>();
  private final Map<Class<?>, MultiFileKey<?>> multiKeyCache = new ConcurrentHashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T> FileKey<T> singleKey(Class<T> configType) {
    return (FileKey<T>) singleKeyCache.computeIfAbsent(configType, fileKeyFactory::singleKey);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> MultiFileKey<T> multiKey(Class<T> configType) {
    return (MultiFileKey<T>) multiKeyCache.computeIfAbsent(configType, fileKeyFactory::multiKey);
  }
}
