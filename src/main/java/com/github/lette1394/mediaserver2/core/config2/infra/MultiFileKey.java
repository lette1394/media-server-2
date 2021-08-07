package com.github.lette1394.mediaserver2.core.config2.infra;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import io.vavr.control.Option;
import java.util.Collection;
import java.util.Map;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class MultiFileKey<T> {
  private final Map<String, FileKey<T>> nameKeyMap;

  MultiFileKey(Collection<FileKey<T>> fileKeys) {
    this.nameKeyMap = fileKeys
      .stream()
      .collect(toMap(this::getFileName, identity()));
  }

  Option<FileKey<T>> belongsTo(String name) {
    return Option.of(nameKeyMap.get(name));
  }

  private String getFileName(FileKey<T> fileKey) {
    return fileKey.path().toFile().getName();
  }
}
