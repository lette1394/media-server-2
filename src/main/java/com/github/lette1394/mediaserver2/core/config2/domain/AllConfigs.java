package com.github.lette1394.mediaserver2.core.config2.domain;

@FunctionalInterface
public interface AllConfigs {
  <T> T find(Key<T> key);
}
