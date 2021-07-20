package com.github.lette1394.mediaserver2.core.configuration.domain;

@FunctionalInterface
public interface AllMultipleResources {
  <T> T find(Class<T> type, String name);
}
