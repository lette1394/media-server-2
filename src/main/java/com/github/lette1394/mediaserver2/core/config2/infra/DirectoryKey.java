package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.Key;
import io.vavr.control.Option;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(of = {"deserializedType", "path"})
@EqualsAndHashCode(of = {"deserializedType", "path"})
final class DirectoryKey<T> implements Key<T> {
  private final Class<T> deserializedType;
  private final String path;
  private final byte[] contents;

  @SuppressWarnings("Convert2MethodRef")
  public DirectoryKey(Class<T> deserializedType, String path) {
    this.deserializedType = deserializedType;
    this.path = path;
    this.contents = Option.of(getClass().getResource(path))
      .toTry()
      .mapTry(URL::toURI)
      .map(Path::of)
      .mapTry(Files::readAllBytes)
      .getOrElseThrow(throwable -> new ConfigException(throwable));
  }
}
