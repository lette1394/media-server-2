package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Jackson implements FileResourceLoader {
  private final ObjectMapper objectMapper;

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException {
    final var path = fileResource.fileResourcePath().toPath();
    final var bytes = Files.readAllBytes(path);
    return objectMapper.readValue(bytes, fileResource.type());
  }
}
