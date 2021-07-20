package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;

@FunctionalInterface
interface FileResourceLoader {
  <T> Try<T> load(FileResource<T> fileResource);
}
