package com.github.lette1394.mediaserver2.core.configuration.domain;

@FunctionalInterface
public interface AllSingleResources {
  <T> T find(Class<T> type);
}
