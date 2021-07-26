package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;

@FunctionalInterface
public interface LocalMetaChanges<T extends Entity> {
  MetaChange<T> within(Trace trace);
}
