package com.github.lette1394.mediaserver2.core.infrastructure;

import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.core.domain.TraceFactory;
import java.util.UUID;

public final class UuidTraceFactory implements TraceFactory {
  @Override
  public Trace create() {
    return new Trace(UUID.randomUUID().toString());
  }
}
