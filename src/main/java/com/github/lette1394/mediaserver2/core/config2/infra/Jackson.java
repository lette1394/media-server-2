package com.github.lette1394.mediaserver2.core.config2.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Jackson implements Loader {
  private final ObjectMapper objectMapper;

  @Override
  public <T> T load(Key<T> key) {
    if (key instanceof FileKey<T> fileKey) {
      try {
        return objectMapper.readValue(fileKey.contents(), fileKey.deserializedType());
      } catch (IOException e) {
        throw new ConfigException(e);
      }
    }
    throw new ConfigException();
  }
}
