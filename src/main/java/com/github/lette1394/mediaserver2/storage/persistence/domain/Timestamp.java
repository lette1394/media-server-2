package com.github.lette1394.mediaserver2.storage.persistence.domain;

import java.time.Instant;

public record Timestamp(
  Instant createdAt,
  Instant updatedAt
) {

  public static Timestamp now() {
    return new Timestamp(Instant.now(), Instant.now());
  }
}
