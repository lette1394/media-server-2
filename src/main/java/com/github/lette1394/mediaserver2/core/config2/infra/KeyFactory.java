package com.github.lette1394.mediaserver2.core.config2.infra;

interface KeyFactory {
  <T> Key<T> create(Class<T> type);

  <T> Key<T> create(Class<T> type, String name);
}
