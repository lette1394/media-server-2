package com.github.lette1394.mediaserver2.core.configuration.domain;

@FunctionalInterface
public interface MappedResource<T> {
  T toMapped();
}
