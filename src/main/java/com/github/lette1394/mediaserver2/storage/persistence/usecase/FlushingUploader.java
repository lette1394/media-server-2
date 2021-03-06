package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import static com.github.lette1394.mediaserver2.core.fluency.domain.FluentCompletionStage.start;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.MetaChanges;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlushingUploader<P extends Payload> implements Uploader<P> {
  private final Uploader<P> uploader;
  private final MetaChanges<?> metaChanges;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    return start()
      .thenCompose(__ -> uploader.upload(command))
      .thenCompose(__ -> metaChanges.flush());
  }
}
