package com.github.lette1394.mediaserver2.storage.usecase;

import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import com.github.lette1394.mediaserver2.storage.domain.NoOpLocker;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.LoggedLocker;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedUploading;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.AllUseCases;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.LockedUploading;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploading;

public class TestUseCases implements AllUseCases {
  @Override
  @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
  public Uploading uploading(Trace trace) {
    final var noOp = new JustReadAllUploading();
    final var locked = new LockedUploading(noOp, locker(trace));
    final var logged = new LoggedUploading(locked, trace);

    return logged;
  }

  @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
  private Locker locker(Trace trace) {
    final var noOp = new NoOpLocker();
    final var logged = new LoggedLocker(noOp, trace);

    return logged;
  }
}
