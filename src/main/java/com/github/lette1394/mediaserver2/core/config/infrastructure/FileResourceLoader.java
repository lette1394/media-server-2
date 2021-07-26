package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;

@FunctionalInterface
interface FileResourceLoader {
  <T> Try<T> load(FileResource<T> fileResource);
}
