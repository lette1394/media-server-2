package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

record FileResource<T>(
  Class<T> type,
  FileResourcePath fileResourcePath
) {
}
