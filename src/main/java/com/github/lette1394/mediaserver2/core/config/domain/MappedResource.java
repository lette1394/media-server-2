package com.github.lette1394.mediaserver2.core.config.domain;

@FunctionalInterface
public interface MappedResource<T> {
  T toMapped();
}
