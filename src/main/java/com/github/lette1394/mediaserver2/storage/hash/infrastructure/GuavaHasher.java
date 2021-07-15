package com.github.lette1394.mediaserver2.storage.hash.infrastructure;

import com.github.lette1394.mediaserver2.storage.hash.domain.HashCode;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class GuavaHasher implements Hasher {
  private final com.google.common.hash.Hasher hasher;

  @Override
  public void update(byte[] bytes) {
    hasher.putBytes(bytes);
  }

  @Override
  public HashCode hash() {
    return new HashCode(hasher.hash().toString());
  }
}
