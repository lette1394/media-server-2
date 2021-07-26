package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.stream.domain.Contracts.requires;

import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
final class FileResource<T> {
  private static final Map<String, ResourceType> EXTENSION_MAP = new HashMap<>();

  static {
    EXTENSION_MAP.put("txt", ResourceType.TEXT);
    EXTENSION_MAP.put("yaml", ResourceType.YAML);
    requires(EXTENSION_MAP.size() == ResourceType.values().length, "missing extension map");
  }

  private final Class<T> type;
  private final FileResourcePath fileResourcePath;
  private final ResourceType resourceType;

  FileResource(Class<T> type, FileResourcePath fileResourcePath) {
    this.type = type;
    this.fileResourcePath = fileResourcePath;
    this.resourceType = fileResourcePath
      .fileExtension()
      .map(EXTENSION_MAP::get)
      .getOrElseThrow(() -> new UnsupportedFileResourceType(
        "unsupported file resource type. filename: [%s]"
          .formatted(fileResourcePath.filename())));
  }

  String filename() {
    return fileResourcePath.filename();
  }

  private static class UnsupportedFileResourceType extends RuntimeException {
    public UnsupportedFileResourceType(String message) {
      super(message);
    }
  }
}