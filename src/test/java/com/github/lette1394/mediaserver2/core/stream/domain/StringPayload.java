package com.github.lette1394.mediaserver2.core.stream.domain;

public record StringPayload(String payload) implements Payload {
  @Override
  public long length() {
    return payload.length();
  }

  @Override
  public void retain() {

  }

  @Override
  public void release() {

  }

  @Override
  public byte[] toBytes() {
    return payload.getBytes();
  }
}
