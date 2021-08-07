package com.github.lette1394.mediaserver2.core.config2.infra;

interface FileKeyFactory {
  <T> FileKey<T> create(Class<T> configType);

  <T> FileKey<T> create(Class<T> configType, String name);
}
