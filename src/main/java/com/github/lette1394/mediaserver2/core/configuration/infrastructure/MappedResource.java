package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

@FunctionalInterface
interface MappedResource<T> {
  T toMapped();
}
