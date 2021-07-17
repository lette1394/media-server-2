package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.storage.hash.domain.Hash;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;

public record Meta(
  Id id,
  long size,
  Timestamp timestamp,
  Hash hash) implements Entity {
}
