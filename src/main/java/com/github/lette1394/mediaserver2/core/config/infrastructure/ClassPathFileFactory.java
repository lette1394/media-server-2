package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;

final class ClassPathFileFactory {
  private static final Map<ConfigType, String> EXTENSION_MAP = new HashMap<>();

  static {
    EXTENSION_MAP.put(ConfigType.JSON, "json");
    EXTENSION_MAP.put(ConfigType.YAML, "yaml");
    requires(EXTENSION_MAP.size() == ConfigType.values().length, "missing extension map");
  }

  private final ClassPathFile basePath;

  public ClassPathFileFactory(ClassPathFile basePath) {
    this.basePath = basePath;
  }

  public Try<ClassPathFile> create(String path) {
    requires(isNotBlank(path), "path must exist");
    return basePath.concat(path);
  }

  public Try<ClassPathFile> create(SingleFileResource annotation) {
    return create(annotation.filePath());
  }

  public Try<ClassPathFile> create(MultiFileResource annotation, String name) {
    final var directoryPath = annotation.directoryPath();
    final var extension = Option
      .of(EXTENSION_MAP.get(annotation.resourceType()))
      .getOrElseThrow(() -> new UnsupportedFileResourceType(
        "unsupported file resource deserializedType. filename: [%s]".formatted(name)));
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
