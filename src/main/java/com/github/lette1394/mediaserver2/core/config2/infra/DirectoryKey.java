package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.config2.infra.ClassPathFileUtils.toClassPath;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import io.vavr.control.Try;
import java.nio.file.Files;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(of = {"deserializedType", "classPath"})
@EqualsAndHashCode(of = {"deserializedType", "classPath"})
final class DirectoryKey<T> implements Key<T> {
  private final Class<T> deserializedType;
  private final String classPath;
  private final Set<FileKey<T>> fileKeySet;

  @SuppressWarnings("Convert2MethodRef")
  DirectoryKey(Class<T> deserializedType, String classPath) {
    this.deserializedType = deserializedType;
    this.classPath = classPath;
    this.fileKeySet = Try.of(() -> Files.walk(toClassPath(classPath)))
      .getOrElseThrow(throwable -> new ConfigException(throwable))
      .filter(path -> path.toFile().isFile())
      .map(path -> new FileKey<>(deserializedType, path))
      .collect(toUnmodifiableSet());
  }

  Set<FileKey<T>> fileKeySet() {
    return fileKeySet;
  }
}
