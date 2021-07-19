package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Option;

@FunctionalInterface
interface AllMappedResourceTypes {
  <T, R extends MappedResource<T>> Option<Class<R>> findMappedResource(Class<T> mappedType);
}
