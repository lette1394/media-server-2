package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

class ClassPathFactory {
  private final ClassPath basePath;

  public ClassPathFactory(String basePath) {
    this.basePath = new ClassPath(basePath);
  }

  public ClassPath create(String path) {
    return basePath.concat(path);
  }

  public ClassPath create(SingleFileResource annotation) {
    final var file = create(annotation.filePath());
    return basePath.concat(file);
  }

  public ClassPath create(MultiFileResource annotation, String name) {
    final var directory = create(annotation.directoryPath());
    final var file = directory.concat("/%s".formatted(name));
    return basePath.concat(file);
  }
}
