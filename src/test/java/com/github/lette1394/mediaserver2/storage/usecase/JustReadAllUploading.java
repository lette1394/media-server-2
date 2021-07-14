package com.github.lette1394.mediaserver2.storage.usecase;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.Uploading;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.UploadingCommand;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class JustReadAllUploading implements Uploading {
  @Override
  public <P extends Payload> CompletionStage<Void> upload(UploadingCommand<P> command) {
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
