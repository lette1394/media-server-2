package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.consumeFinally;
import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockedUploading implements Uploading {
  private final Uploading uploading;
  private final Locker locker;

  @Override
  public <P extends Payload> CompletionStage<Void> upload(UploadingCommand<P> command) {
    return start()
      .thenCompose(__ -> locker.lock())
      .thenCompose(__ -> uploading.upload(command))
      .whenComplete(consumeFinally(__ -> locker.unlock()));
  }
}
