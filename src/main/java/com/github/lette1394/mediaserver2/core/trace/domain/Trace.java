package com.github.lette1394.mediaserver2.core.trace.domain;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Trace {
  private final String id;

  Trace(String id) {
    requires(isNotBlank(id), "isNotBlank(id)");
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }
}
