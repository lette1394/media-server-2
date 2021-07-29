package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Option;

@FunctionalInterface
interface AllRawConfigTypes {
  <T, R extends T> Option<Class<R>> findRawConfig(Class<T> mappedType);
}
