package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;

@FunctionalInterface
public interface Uploaders<P extends Payload> {
  Uploader<P> within(Trace trace);
}
