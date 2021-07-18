package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassPaths {
  private final ClassPath base;

  public ClassPath create(String path) {
    return base.concat(new ClassPath(path));
  }
}
