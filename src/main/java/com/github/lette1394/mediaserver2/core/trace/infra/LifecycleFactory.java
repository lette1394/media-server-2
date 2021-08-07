package com.github.lette1394.mediaserver2.core.trace.infra;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceFactory;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceLifecycle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LifecycleFactory extends TraceFactory {
  private final TraceFactory traceFactory;
  // 생성할 때 하지말고 reactor 내부에서 하자
  private final TraceLifecycle.AfterCreated afterCreated;

  @Override
  public Trace newTrace() {
    final var trace = traceFactory.newTrace();
    afterCreated.afterCreated(trace);
    return trace;
  }
}
