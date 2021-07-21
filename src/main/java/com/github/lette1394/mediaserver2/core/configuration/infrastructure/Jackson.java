package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.configuration.domain.SerializedType;
import io.vavr.control.Try;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Jackson implements FileResourceLoader {
  private final ObjectMapper objectMapper;

  @Override
  @SuppressWarnings("unchecked")
  public <T> Try<T> load(FileResource<T> fileResource) {
    final var annotation = fileResource.type().getAnnotation(SerializedType.class);

    if (annotation == null) {
      return Try.of(() -> {
        final var path = fileResource.fileResourcePath().toPath();
        final var bytes = Files.readAllBytes(path);
        return objectMapper.readValue(bytes, fileResource.type());
      });
    }

    return Try.of(() -> {
      final var path = fileResource.fileResourcePath().toPath();
      final var bytes = Files.readAllBytes(path);
      return (T) objectMapper.readValue(bytes, annotation.value());
    });
  }
}
