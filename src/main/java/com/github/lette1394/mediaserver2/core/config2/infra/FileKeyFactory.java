package com.github.lette1394.mediaserver2.core.config2.infra;

interface FileKeyFactory {
  <T> FileKey<T> singleKey(Class<T> configType);

  <T> FileKey<T> multiKey(Class<T> configType, String name);
}
