package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Option;

@FunctionalInterface
interface AllTypeAliases {
  <T, R extends T> Option<Class<R>> findTypeAlias(Class<T> sourceType);
}
