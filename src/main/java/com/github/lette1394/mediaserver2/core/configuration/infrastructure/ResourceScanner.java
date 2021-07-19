package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.util.Set;

@FunctionalInterface
interface ResourceScanner {
  Set<? extends FileResource<?>> scan();
}
