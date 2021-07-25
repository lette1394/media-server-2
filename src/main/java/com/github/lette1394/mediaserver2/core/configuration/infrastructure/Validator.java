package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import io.vavr.API;
import io.vavr.control.Try;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Validator implements FileResourceLoader {
  private final FileResourceLoader loader;
  private final JsonSchemaFactory factory;
  private final ObjectMapper objectMapper;
  private final FileResourcePathFactory fileResourcePathFactory;

  @Override
  public <T> Try<T> load(FileResource<T> fileResource) {
    final var annotation = fileResource.type().getAnnotation(JsonSchema.class);
    if (annotation == null) {
      return loader.load(fileResource);
    }

    final var jsonPath = fileResource.fileResourcePath().toPath();
    final var jsonNode = Try
      .of(() -> Files.readAllBytes(jsonPath))
      .mapTry(objectMapper::readTree);

    final var jsonSchema = fileResourcePathFactory
      .create(annotation.schemaFilePath())
      .map(FileResourcePath::toPath)
      .mapTry(Files::readAllBytes)
      .map(String::new)
      .map(factory::getSchema);

    //noinspection Convert2MethodRef
    return API
      .For(jsonNode, jsonSchema)
      .yield((node, schema) -> schema.validate(node))
      .filter(errors -> errors.isEmpty(), errors -> new IllegalJsonSchemaException(errors.toString(), fileResource))
      .flatMap(errors -> loader.load(fileResource));
  }

  private static final class IllegalJsonSchemaException extends RuntimeException {
    public IllegalJsonSchemaException(String message, FileResource<?> fileResource) {
      super("validation failed: [%s], at: [%s]".formatted(message, fileResource));
    }
  }
}
