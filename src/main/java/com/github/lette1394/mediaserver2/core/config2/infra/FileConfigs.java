package com.github.lette1394.mediaserver2.core.config2.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.config2.domain.AllConfigs;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.Key;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileConfigs implements AllConfigs {
  private final ObjectMapper objectMapper;

  @Override
  public <T> T find(Key<T> key) {
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
