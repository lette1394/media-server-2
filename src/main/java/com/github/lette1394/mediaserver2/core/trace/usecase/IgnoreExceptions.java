package com.github.lette1394.mediaserver2.core.trace.usecase;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceLifecycle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IgnoreExceptions implements TraceLifecycle.AfterCreated,
                                         TraceLifecycle.BeforeDisposing,
                                         TraceLifecycle.AfterDisposed {
  private final TraceLifecycle.AfterCreated afterCreated;
  private final TraceLifecycle.BeforeDisposing beforeDisposing;
  private final TraceLifecycle.AfterDisposed afterDisposed;

  @Override
  public void afterCreated(Trace trace) {
    try {
      afterCreated.afterCreated(trace);
    } catch (Exception ignored) {
    }
  }

  @Override
  public void beforeDisposing(Trace trace) {
    try {
      beforeDisposing.beforeDisposing(trace);
    } catch (Exception ignored) {
    }
  }

  @Override
  public void afterDisposed(Trace trace) {
    try {
      afterDisposed.afterDisposed(trace);
    } catch (Exception ignored) {
    }
  }
}
