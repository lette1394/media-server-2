package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

class ClassPathFactory {
  private final ClassPath basePath;

  public ClassPathFactory(String basePath) {
    this.basePath = new ClassPath(basePath);
  }

  public ClassPath create(String path) {
    return basePath.concat(path);
  }
}
