package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileResourceLoaders {
  private final ObjectMapper objectMapper;
  private final Warmer warmer;

  Try<FileResourceLoader> create(ResourceScanner resourceScanner) {
    final var jackson = new Jackson(objectMapper);
    final var logged = new Logging(jackson);
    final var thread = new ThreadSafe(logged);
    final var cached = new Caching(thread);
    final var warmed = WarmingUp.create(cached, resourceScanner, warmer);

    return warmed;
  }
}
