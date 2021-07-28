package com.github.lette1394.mediaserver2.core.trace.domain;

public interface BeforeEndingCallback extends TraceLifecycle {
  void beforeEnding(Trace trace);
}
