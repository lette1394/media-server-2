package com.github.lette1394.mediaserver2.storage.usecase;

import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import com.github.lette1394.mediaserver2.storage.domain.NoOpLocker;
import com.github.lette1394.mediaserver2.storage.lock.infrastructure.LoggedLocker;
import com.github.lette1394.mediaserver2.storage.persistence.infrastructure.LoggedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.AllUseCases;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.LockedUploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;

public class TestUseCases implements AllUseCases {
  @Override
  @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
  public Uploader uploading(Trace trace) {
    final var noOp = new JustReadAllUploader();
    final var locked = new LockedUploader(noOp, locker(trace));
    final var logged = new LoggedUploader(locked, trace);

    return logged;
  }

  @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
  private Locker locker(Trace trace) {
    final var noOp = new NoOpLocker();
    final var logged = new LoggedLocker(noOp, trace);

    return logged;
  }
}
