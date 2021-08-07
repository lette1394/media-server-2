package com.github.lette1394.mediaserver2.core.config2.infra;

import java.util.Map;

interface FileKeyFactory {
  <T> FileKey<T> singleKey(Class<T> configType);

  <T> Map<String, FileKey<T>> multiKey(Class<T> configType);
}
