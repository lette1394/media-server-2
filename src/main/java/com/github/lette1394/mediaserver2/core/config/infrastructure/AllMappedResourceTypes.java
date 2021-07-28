package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.RawConfig;
import io.vavr.control.Option;

@FunctionalInterface
interface AllMappedResourceTypes {
  <T, R extends RawConfig<T>> Option<Class<R>> findMappedResource(Class<T> mappedType);
}
