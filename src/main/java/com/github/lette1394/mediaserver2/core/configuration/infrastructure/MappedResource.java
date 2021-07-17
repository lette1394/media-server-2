package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

@FunctionalInterface
public interface MappedResource<T> {
  T toMapped();
}
