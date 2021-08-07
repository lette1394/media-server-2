package com.github.lette1394.mediaserver2.core.config2.infra;

interface FileKeyFactory {
  <T> FileKey<T> create(Class<T> deserializedType);

  <T> FileKey<T> create(Class<T> deserializedType, String name);
}
