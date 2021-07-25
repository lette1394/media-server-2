package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FileResourceLoaders {
  private final ObjectMapper objectMapper;
  private final Warmer warmer;
  private final JsonSchemaFactory jsonSchemaFactory;
  private final FileResourcePathFactory fileResourcePathFactory;

  Try<FileResourceLoader> create(ResourceScanner resourceScanner) {
    final var jackson = new Jackson(objectMapper);
    final var valid = new Validator(jackson, jsonSchemaFactory, objectMapper, fileResourcePathFactory);
    final var logged = new Logging(valid);
    final var thread = new ThreadSafe(logged);
    final var cached = new Caching(thread);
    final var warmed = WarmingUp.create(cached, resourceScanner, warmer);

    return warmed;
  }
}
