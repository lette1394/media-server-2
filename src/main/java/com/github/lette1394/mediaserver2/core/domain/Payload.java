package com.github.lette1394.mediaserver2.core.domain;

public interface Payload {
  long length();

  void retain();

  void release();

  byte[] toBytes();
}
