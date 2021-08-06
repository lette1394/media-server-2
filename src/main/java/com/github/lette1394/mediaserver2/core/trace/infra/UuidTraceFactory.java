package com.github.lette1394.mediaserver2.core.trace.infra;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.domain.TraceFactory;
import java.util.UUID;

public final class UuidTraceFactory extends TraceFactory {
  @Override
  public Trace newTrace() {
    return traceConstructor(UUID.randomUUID().toString());
  }
}
