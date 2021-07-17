package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.checkedRequires;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class JacksonFileLoader implements UnsafeFileResources {
  private final ObjectMapper objectMapper;

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException, URISyntaxException {
    final var classPath = fileResource.classPath();
    final var resource = getClass().getResource(classPath.rawPath());
    checkedRequires(resource != null,
      () -> new FileNotFoundException("classpath:[%s] must be exist".formatted(classPath)));

    final var path = Paths.get(resource.toURI());
    final var bytes = Files.readAllBytes(path);
    return objectMapper.readValue(bytes, fileResource.type());
  }
}
