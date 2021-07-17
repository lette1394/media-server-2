package com.github.lette1394.mediaserver2.core.configuration.domain;

import io.vavr.control.Option;

@FunctionalInterface
public interface AllMultipleResources {
  <T> Option<T> find(Class<T> type, String name);
}
