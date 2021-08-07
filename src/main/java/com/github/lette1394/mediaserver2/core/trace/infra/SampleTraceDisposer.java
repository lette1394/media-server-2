package com.github.lette1394.mediaserver2.core.trace.infra;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceDisposer;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceLifecycle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SampleTraceDisposer implements TraceDisposer {
  private final TraceLifecycle.AfterDisposed afterDisposed;

  @Override
  public void dispose(Trace trace) {
    afterDisposed.afterDisposed(trace);
  }
}
