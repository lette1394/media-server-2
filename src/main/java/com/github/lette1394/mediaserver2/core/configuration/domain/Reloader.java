package com.github.lette1394.mediaserver2.core.configuration.domain;

import io.vavr.control.Try;

@FunctionalInterface
public interface Reloader {
  Try<Void> reload();
}
