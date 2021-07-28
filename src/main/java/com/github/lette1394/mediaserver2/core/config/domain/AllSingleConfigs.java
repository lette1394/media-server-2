package com.github.lette1394.mediaserver2.core.config.domain;

@FunctionalInterface
public interface AllSingleConfigs {
  <T> T find(Class<T> type) throws CannotFindConfigException;
}
