package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.config.infrastructure.WarmingUp.createWarmingUp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class DeserializerFactory {
  private final ObjectMapper objectMapper;
  private final Warmer warmer;
  private final JsonSchemaFactory jsonSchemaFactory;
  private final ClassPathFileFactory classPathFileFactory;

  @SuppressWarnings("UnnecessaryLocalVariable")
  Try<Deserializer> create(ResourceScanner resourceScanner) {
    final var jackson = new Jackson(objectMapper);
    final var valid = new Validator(jackson, jsonSchemaFactory, objectMapper, classPathFileFactory);
    final var logged = new Logging(valid);
    final var thread = new ThreadSafe(logged);
    final var cached = new Caching(thread);
    final var warmed = createWarmingUp(cached, resourceScanner, warmer);

    return warmed;
  }
}
