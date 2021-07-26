package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.storage.persistence.domain.AllBinaries;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class DrainingAllBinaries<P extends Payload> implements AllBinaries<P> {
  @Override
  public CompletionStage<BinaryPublisher<P>> find(Id id) {
    return CompletableFuture.completedStage(null);
  }

  @Override
  public CompletionStage<Void> save(Id id, BinaryPublisher<P> publisher) {
    final var result = new CompletableFuture<Void>();

    Flux
      .from(publisher)
      .doOnNext(Payload::release)
      .doOnComplete(() -> result.complete(null))
      .subscribeOn(Schedulers.boundedElastic())
      .subscribe();

    return result;
  }
}
