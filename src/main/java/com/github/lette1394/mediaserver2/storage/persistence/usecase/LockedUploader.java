package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.consumeFinally;
import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.lock.domain.Locker;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockedUploader<P extends Payload> implements Uploader<P> {
  private final Uploader<P> uploader;
  private final Locker locker;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    return start()
      .thenCompose(__ -> locker.lock())
      .thenCompose(__ -> uploader.upload(command))
      .whenComplete(consumeFinally(__ -> locker.unlock()));
  }
}
