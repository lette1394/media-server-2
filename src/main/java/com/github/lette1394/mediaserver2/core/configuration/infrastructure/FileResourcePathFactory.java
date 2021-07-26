package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.stream.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;

final class FileResourcePathFactory {
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
    requires(isNotBlank(path), "path must exist");
    return basePath.concat(path);
  }

  public Try<FileResourcePath> create(SingleFileResource annotation) {
    return create(annotation.filePath());
  }

  public Try<FileResourcePath> create(MultiFileResource annotation, String name) {
    final var directoryPath = annotation.directoryPath();
    final var extension = Option
      .of(EXTENSION_MAP.get(annotation.resourceType()))
      .getOrElseThrow(() -> new UnsupportedFileResourceType(
        "unsupported file resource type. filename: [%s]".formatted(name)));
    final var dotExtension = ".%s".formatted(extension);

    if (name.endsWith(dotExtension)) {
      return create("%s/%s".formatted(directoryPath, name));
    }
    return create("%s/%s".formatted(directoryPath, name + dotExtension));
  }

  private static class UnsupportedFileResourceType extends RuntimeException {
    public UnsupportedFileResourceType(String message) {
      super(message);
    }
  }
}
