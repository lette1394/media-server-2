package com.github.lette1394.mediaserver2.core.config2.domain;

public interface AllConfigs {
  <T> T find(Class<T> deserializedType);

  <T> T find(Class<T> deserializedType, String name);
}
