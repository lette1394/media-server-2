package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;

import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;

class FileResourcePathFactory {
  private static final Map<ResourceType, String> EXTENSION_MAP = new HashMap<>();

  static {
    EXTENSION_MAP.put(ResourceType.TEXT, "txt");
    EXTENSION_MAP.put(ResourceType.YAML, "yaml");
    requires(EXTENSION_MAP.size() == ResourceType.values().length, "missing extension map");
  }

  private final FileResourcePath basePath;

  public FileResourcePathFactory(FileResourcePath basePath) {
    this.basePath = basePath;
  }

  public Try<FileResourcePath> create(String path) {
    return basePath.concat(path);
  }

  public Try<FileResourcePath> create(SingleFileResource annotation) {
    return create(annotation.filePath());
  }

  public Try<FileResourcePath> create(MultiFileResource annotation, String name) {
//    Option.of(EXTENSION_MAP.get(annotation.resourceType()))
//      .map()

    return create(annotation.directoryPath())
      .flatMap(path -> path.concat("/%s".formatted(name)));
  }
}
