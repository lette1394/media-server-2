package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
final class FileConfig<T> {
  private static final Map<String, ConfigType> CONFIG_TYPE_MAP = new HashMap<>();

  static {
    CONFIG_TYPE_MAP.put("json", ConfigType.JSON);
    CONFIG_TYPE_MAP.put("yaml", ConfigType.YAML);
    requires(CONFIG_TYPE_MAP.size() == ConfigType.values().length, "missing config deserializedType map");
  }

  private final Class<T> deserializedType;
  private final ClassPathFile classPathFile;

  FileConfig(Class<T> deserializedType, ClassPathFile classPathFile) {
    this.deserializedType = deserializedType;
    this.classPathFile = classPathFile;

    final var extension = classPathFile.extension().getOrElseThrow(unsupported("<no extension>"));
    requires(CONFIG_TYPE_MAP.containsKey(extension), unsupported(extension));
  }

  private Supplier<UnsupportedFileResourceType> unsupported(String extension) {
    return () -> new UnsupportedFileResourceType("unsupported file extension: [%s]".formatted(extension));
  }

  Class<T> deserializedType() {
    return deserializedType;
  }

  String filename() {
    return classPathFile.filename();
  }

  byte[] contents() {
    return classPathFile.contents();
  }

  private static class UnsupportedFileResourceType extends RuntimeException {
    public UnsupportedFileResourceType(String message) {
      super(message);
    }
  }
}