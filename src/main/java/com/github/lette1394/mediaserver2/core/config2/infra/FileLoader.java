package com.github.lette1394.mediaserver2.core.config2.infra;

@FunctionalInterface
interface FileLoader {
  <T> T load(FileKey<T> key);
}
