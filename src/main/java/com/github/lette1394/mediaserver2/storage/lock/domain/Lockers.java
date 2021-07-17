package com.github.lette1394.mediaserver2.storage.lock.domain;

import com.github.lette1394.mediaserver2.core.domain.Trace;

@FunctionalInterface
public interface Lockers {
  Locker within(Trace trace);
}
