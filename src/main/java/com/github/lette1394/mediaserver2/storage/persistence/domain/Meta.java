package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.storage.hash.domain.HashCode;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Meta {
  private final Id id;
  private final long size;
  private final Timestamp timestamp;
  private final HashCode hashCode;
}
