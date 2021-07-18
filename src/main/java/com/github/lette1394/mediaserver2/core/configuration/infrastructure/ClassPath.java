package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import io.vavr.control.Option;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

class ClassPath {
  private final String stringPath;
  private final Path path;

  ClassPath(String path) {
    requires(isNoneBlank(path), "isNoneBlank(path)");
    requires(path.startsWith("/"), "path.startsWith(\"/\")");
    requires(!path.endsWith("/"), "!path.endsWith(\"/\")");

    this.stringPath = path;
    this.path = Option
      .of(getClass().getResource(path))
      .toTry(() -> new FileNotFoundException("illegal path: [%s]".formatted(path)))
      .mapTry(URL::toURI)
      .map(Paths::get)
      .get();
  }

  ClassPath concat(ClassPath other) {
    return new ClassPath(this.stringPath + other.stringPath);
  }

  ClassPath concat(String path) {
    return new ClassPath(this.stringPath + path);
  }

  public Path toPath() {
    return path;
  }

  public boolean endsWith(String path) {
    return stringPath.endsWith(path);
  }

  @Override
  public String toString() {
    return stringPath;
  }
}
