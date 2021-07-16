package com.github.lette1394.mediaserver2.storage.hash.domain;

public interface Hasher {
  void update(byte[] bytes);

  Hash hash();
}
