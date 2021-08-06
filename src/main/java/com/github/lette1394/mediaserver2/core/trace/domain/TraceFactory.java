package com.github.lette1394.mediaserver2.core.trace.domain;

public abstract class TraceFactory {
  public abstract Trace newTrace();

  protected Trace traceConstructor(String id) {
    return new Trace(id);
  }
}
