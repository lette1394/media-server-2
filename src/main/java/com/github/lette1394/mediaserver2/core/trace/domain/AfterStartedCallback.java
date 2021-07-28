package com.github.lette1394.mediaserver2.core.trace.domain;

public interface AfterStartedCallback extends TraceLifecycle {
  void afterStarted(Trace trace);
}
