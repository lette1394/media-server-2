package com.github.lette1394.mediaserver2.core.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Trace {
  private final String id;

  public Trace(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }
}
