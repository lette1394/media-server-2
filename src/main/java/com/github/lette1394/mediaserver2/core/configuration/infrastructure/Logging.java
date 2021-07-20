package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class Logging implements FileResourceLoader {
  private final FileResourceLoader loader;

  @Override
  public <T> T load(FileResource<T> fileResource) throws IOException {
    try {
      final T result = loader.load(fileResource);
      log.info("loaded: [{}]", fileResource);
      return result;
    } catch (Exception exception) {
      log.error("got exception when loading: [{}], cause: [{}]", fileResource, exception);
      throw exception;
    }
  }
}
