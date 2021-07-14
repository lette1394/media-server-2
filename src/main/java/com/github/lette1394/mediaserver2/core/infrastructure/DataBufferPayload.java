package com.github.lette1394.mediaserver2.core.infrastructure;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

public record DataBufferPayload(DataBuffer dataBuffer) implements Payload {
  @Override
  public long length() {
    return dataBuffer.readableByteCount();
  }

  @Override
  public void retain() {
    DataBufferUtils.retain(dataBuffer);
  }

  @Override
  public void release() {
    DataBufferUtils.release(dataBuffer);
  }

  @Override
  public byte[] toBytes() {
    final var start = dataBuffer.readPosition();
    final var length = dataBuffer.readableByteCount();
    final var result = new byte[length];
    final var slice = dataBuffer.slice(start, length);
    slice.read(result);

    return result;
  }
}
