package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.Resource;
import lombok.RequiredArgsConstructor;

public class YamlResource implements Resource {
  private final byte[] contents;

  public YamlResource(byte[] contents) {
    this.contents = contents;
  }

  @Override
  public byte[] contents() {
    return new byte[0];
  }
}
