package com.github.lette1394.mediaserver2.core.config.infrastructure;

import io.vavr.control.Try;
import java.util.Set;

@FunctionalInterface
interface ResourceScanner {
  Try<Set<? extends FileConfig<?>>> scan();
}
