package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.Resource;
import java.nio.charset.StandardCharsets;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ByteArrayResource implements Resource {
  private final byte[] contents;

  public ByteArrayResource(byte[] contents) {
    this.contents = contents;
  }

  public ByteArrayResource(String contents) {
    this(contents.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public byte[] contents() {
    return contents;
  }

  @Override
  public String toString() {
    return new String(contents);
  }
}
