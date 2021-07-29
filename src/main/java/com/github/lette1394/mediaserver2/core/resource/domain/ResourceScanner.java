package com.github.lette1394.mediaserver2.core.resource.domain;

import io.vavr.control.Try;
import java.util.Set;

@FunctionalInterface
public interface ResourceScanner {
  Try<Set<? extends Resource>> scan();
}
