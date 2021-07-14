package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.Uploading;

public interface AllUseCases {
  Uploading uploading(Trace trace);
}
