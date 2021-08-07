package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.config2.infra.ClassPathFileUtils.readAllBytes;
import static com.github.lette1394.mediaserver2.core.config2.infra.ClassPathFileUtils.toClassPath;
import static java.util.stream.Collectors.toUnmodifiableSet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

@ToString(of = {"deserializedType", "classPath"})
@EqualsAndHashCode(of = {"deserializedType", "classPath"})
final class FileKey<T> implements Key<T> {
  private final Class<T> deserializedType;
  private final Path classPath;
  private final byte[] contents;

  private FileKey(Class<T> deserializedType, Path classPath, byte[] contents) {
    this.deserializedType = deserializedType;
    this.classPath = classPath;
    this.contents = contents;
  }

  @SneakyThrows
  static <T> Set<FileKey<T>> scanFileKey(Class<T> deserializedType, String classPath) {
    return Files.walk(toClassPath(classPath))
      .filter(path -> path.toFile().isFile())
      .map(path -> new FileKey<>(deserializedType, path))
      .collect(toUnmodifiableSet());
  }

  FileKey(Class<T> deserializedType, Path classPath) {
    this(deserializedType, classPath, readAllBytes(classPath));
  }

  FileKey(Class<T> deserializedType, String classPath) {
    this(deserializedType, toClassPath(classPath), readAllBytes(classPath));
  }

  Class<T> deserializedType() {
    return deserializedType;
  }

  byte[] contents() {
    return contents;
  }

  Path path() {
    return classPath;
  }
}
