package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Option;

@FunctionalInterface
interface AllEntityTypes {
  <T, R extends Entity<T>> Option<Class<R>> relatedEntityType(Class<T> type);
}
