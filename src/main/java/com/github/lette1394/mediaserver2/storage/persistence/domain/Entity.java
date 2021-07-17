package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.storage.identification.domain.Id;

@FunctionalInterface
public interface Entity {
  Id id();
}
