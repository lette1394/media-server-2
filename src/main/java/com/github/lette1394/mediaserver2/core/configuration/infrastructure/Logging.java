package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class Logging implements FileResourceLoader {
  private final FileResourceLoader loader;

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    return loader.load(fileResource)
      .onSuccess(__ -> log.info("loaded: [{}]", fileResource))
      .onFailure(throwable -> log.error("got exception during loading: [{}], cause: [{}]",
        fileResource, throwable));
  }
}
