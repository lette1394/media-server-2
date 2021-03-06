package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

final class ClassPaths {
  private ClassPaths() {
  }

  @SuppressWarnings("Convert2MethodRef")
  static Path toClassPath(String classPath) {
    return Option.of(ClassPaths.class.getResource(classPath))
      .toTry(() -> new ConfigException("classPath:[%s] not found".formatted(classPath)))
      .mapTry(URL::toURI)
      .map(Path::of)
      .getOrElseThrow(throwable -> new ConfigException(throwable));
  }

  static byte[] readAllBytes(String classPath) {
    return readAllBytes(toClassPath(classPath));
  }

  static byte[] readAllBytes(Path path) {
    requires(path.toFile().isFile(), "given path should be a file, not directory");
    return Try.of(() -> Files.readAllBytes(path))
      .getOrElseThrow(throwable -> new ConfigException("path:[%s]".formatted(path), throwable));
  }
}
