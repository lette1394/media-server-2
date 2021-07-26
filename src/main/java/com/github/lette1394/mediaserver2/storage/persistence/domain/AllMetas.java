package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import java.util.concurrent.CompletionStage;

public interface AllMetas<T extends Meta> {
  CompletionStage<T> belongsTo(Id id);
}
