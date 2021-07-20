package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

class FileResourcePathFactory {
  private final FileResourcePath basePath;

  public FileResourcePathFactory(String basePath) {
    this.basePath = new FileResourcePath(basePath);
  }

  public FileResourcePath create(String path) {
    return basePath.concat(path);
  }

  public FileResourcePath create(SingleFileResource annotation) {
    return create(annotation.filePath());
  }

  public FileResourcePath create(MultiFileResource annotation, String name) {
    final var directory = create(annotation.directoryPath());
    return directory.concat("/%s".formatted(name));
  }
}
