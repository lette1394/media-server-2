package com.github.lette1394.mediaserver2.core.trace.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Trace {
  private final String id;

  Trace(String id) {
    this.id = id;
  }

  // public modifier 가 아니면서, spring controller aop 에 일괄 적용할 수 있을까?
  void finish() {

  }

  @Override
  public String toString() {
    return id;
  }
}
