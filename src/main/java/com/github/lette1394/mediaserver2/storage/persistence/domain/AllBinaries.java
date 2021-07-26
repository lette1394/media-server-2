package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import java.util.concurrent.CompletionStage;

public interface AllBinaries<P extends Payload> {
  CompletionStage<BinaryPublisher<P>> find(Id id);

  CompletionStage<Void> save(Id id, BinaryPublisher<P> publisher);
}
