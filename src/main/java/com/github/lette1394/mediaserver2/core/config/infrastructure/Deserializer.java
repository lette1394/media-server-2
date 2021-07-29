package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;

@FunctionalInterface
interface Deserializer {
  <T> Try<T> deserialize(FileConfig<T> fileConfig);
}
