package com.github.lette1394.mediaserver2.core.config.domain;

@FunctionalInterface
public interface AllMultiConfigs {
  <T> T find(Class<T> type, String name) throws CannotFindConfigException;
}
