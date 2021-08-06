package com.github.lette1394.mediaserver2.core.trace.domain;

public interface TraceLifecycle {
  interface AfterCreated {
    void afterCreated(Trace trace);
  }

  interface BeforeDisposing {
    void beforeDisposing(Trace trace);
  }

  interface AfterDisposed {
    void afterDisposed(Trace trace);
  }
}
