package com.github.lette1394.mediaserver2.storage.hash.infrastructure;

import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.google.common.hash.Hashing;

@SuppressWarnings("UnstableApiUsage")
public final class GuavaHashers {
  public Hasher md5() {
    return new GuavaHasher(Hashing.md5().newHasher());
  }

  public Hasher sha256() {
    return new GuavaHasher(Hashing.sha256().newHasher());
  }

  public Hasher sha512() {
    return new GuavaHasher(Hashing.sha512().newHasher());
  }
}
