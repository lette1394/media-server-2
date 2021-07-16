package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.domain.Trace;

@FunctionalInterface
public interface LocalMetaChanges<T> {
  MetaChange<T> within(Trace trace);
}
