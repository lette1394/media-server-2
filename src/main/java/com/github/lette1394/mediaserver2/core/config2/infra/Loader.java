package com.github.lette1394.mediaserver2.core.config2.infra;

@FunctionalInterface
interface Loader {
  <T> T load(Key<T> key);
}
