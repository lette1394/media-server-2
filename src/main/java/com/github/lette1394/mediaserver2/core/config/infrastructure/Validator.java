package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import io.vavr.API;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Validator implements Deserializer {
  private final Deserializer deserializer;
  private final JsonSchemaFactory factory;
  private final ObjectMapper objectMapper;
  private final ClassPathFileFactory classPathFileFactory;

  @Override
  public <T> Try<T> deserialize(FileConfig<T> fileConfig) {
    final var annotation = fileConfig.deserializedType().getAnnotation(JsonSchema.class);
    if (annotation == null) {
      return deserializer.deserialize(fileConfig);
    }

    final var jsonNode = Try.of(() -> objectMapper.readTree(fileConfig.contents()));
    final var jsonSchema = classPathFileFactory.create(annotation.schemaFilePath())
      .map(ClassPathFile::contents)
      .map(String::new)
      .map(factory::getSchema);

    //noinspection Convert2MethodRef
    return API
      .For(jsonNode, jsonSchema)
      .yield((node, schema) -> schema.validate(node))
      .filter(errors -> errors.isEmpty(), errors -> new IllegalJsonSchemaException(errors.toString(), fileConfig))
      .flatMap(errors -> deserializer.deserialize(fileConfig));
  }

  private static final class IllegalJsonSchemaException extends RuntimeException {
    public IllegalJsonSchemaException(String message, FileConfig<?> fileConfig) {
      super("json schema validation failed: [%s], at: [%s]".formatted(message, fileConfig));
    }
  }
}
