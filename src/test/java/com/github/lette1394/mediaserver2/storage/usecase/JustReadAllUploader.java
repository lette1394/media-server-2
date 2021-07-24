package com.github.lette1394.mediaserver2.storage.usecase;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class JustReadAllUploader<P extends Payload> implements Uploader<P> {
  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    final CompletableFuture<Void> result = new CompletableFuture<>();

    command.binaryPublisher().subscribe(new Subscriber<>() {
      @Override
      public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(P item) {
        // no-op
      }

      @Override
      public void onError(Throwable throwable) {
        result.completeExceptionally(throwable);
      }

      @Override
      public void onComplete() {
        result.complete(null);
      }
    });

    return result;
  }
}
