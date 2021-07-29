package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;


@EqualsAndHashCode
final class ClassPathFile {
  private final String stringPath;
  private final Path path;

  @SneakyThrows
  private ClassPathFile(String path) {
    requires(isNoneBlank(path), "isNoneBlank(path)");
    requires(path.startsWith("/"), "path.startsWith(\"/\")");
    requires(!path.endsWith("/"), "!path.endsWith(\"/\")");
    final var resource = getClass().getResource(path);
    requires(resource != null, () -> new FileNotFoundException("not found: [%s]".formatted(path)));

    this.stringPath = path;
    this.path = Paths.get(resource.toURI());
  }

  static Try<ClassPathFile> createClassPathFile(String path) {
    return Try.of(() -> new ClassPathFile(path));
  }

  Try<ClassPathFile> concat(String path) {
    return Try.of(() -> new ClassPathFile(this.stringPath + path));
  }

  Path toJavaPath() {
    return path;
  }

  String filename() {
    return path.toFile().getName();
  }

  @SneakyThrows
  byte[] contents() {
    return Files.readAllBytes(this.path);
  }

  Option<String> extension() {
    return Option
      .of(getExtension(stringPath))
      .filter(extension -> !extension.isEmpty());
  }

  @Override
  public String toString() {
    return stringPath;
  }
}
