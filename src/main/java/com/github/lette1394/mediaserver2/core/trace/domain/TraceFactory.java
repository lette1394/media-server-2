package com.github.lette1394.mediaserver2.core.trace.domain;

@FunctionalInterface
public interface TraceFactory {
  Trace newTrace();
}
