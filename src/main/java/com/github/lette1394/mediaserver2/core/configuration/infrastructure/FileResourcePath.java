package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode
class FileResourcePath {
  private final String stringPath;
  private final Path path;

  private FileResourcePath(String path) {
    requires(isNoneBlank(path), "isNoneBlank(path)");
    requires(path.startsWith("/"), "path.startsWith(\"/\")");
    requires(!path.endsWith("/"), "!path.endsWith(\"/\")");

    this.stringPath = path;
    this.path = Option
      .of(getClass().getResource(path))
      .toTry(() -> new FileNotFoundException("not found: [%s]".formatted(path)))
      .mapTry(URL::toURI)
      .map(Paths::get)
      .get();
  }

  Try<FileResourcePath> concat(String path) {
    return Try.of(() -> new FileResourcePath(this.stringPath + path));
  }

  static Try<FileResourcePath> create(String path) {
    return Try.of(() -> new FileResourcePath(path));
  }

  Path toPath() {
    return path;
  }

  String filename() {
    return path.toFile().getName();
  }

  Option<String> fileExtension() {
    return Option.of(stringPath)
      .filter(path -> path.contains("."))
      .map(path -> path.substring(stringPath.lastIndexOf(".") + 1));
  }

  @Override
  public String toString() {
    return stringPath;
  }
}
