package com.github.lette1394.mediaserver2.core.resource.infra;

import com.github.lette1394.mediaserver2.core.resource.domain.Resource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ByteArrayResource implements Resource {
  private final byte[] contents;

  @Override
  public byte[] contents() {
    return contents;
  }
}
