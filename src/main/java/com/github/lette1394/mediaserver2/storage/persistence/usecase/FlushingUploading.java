package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChange;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploading;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlushingUploading<P extends Payload> implements Uploading<P> {
  private final Uploading<P> uploading;
  private final MetaChange<P> metaChange;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    return start()
      .thenCompose(__ -> uploading.upload(command))
      .thenCompose(__ -> metaChange.flushToAll());
  }
}
