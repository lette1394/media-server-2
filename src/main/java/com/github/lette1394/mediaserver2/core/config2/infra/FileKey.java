package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.config2.infra.ClassPathFileUtils.readAllBytes;
import static com.github.lette1394.mediaserver2.core.config2.infra.ClassPathFileUtils.toClassPath;

import com.github.lette1394.mediaserver2.core.config2.domain.Key;
import java.nio.file.Path;
import lombok.EqualsAndHashCode;
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
}
