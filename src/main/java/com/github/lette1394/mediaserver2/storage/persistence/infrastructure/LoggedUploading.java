package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploading;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedUploading<P extends Payload> implements Uploading<P> {
  private final Uploading<P> uploading;
  private final Trace trace;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    final var binaryPublisher = command.binaryPublisher();
    final var logged = new LoggedBinaryPublisher<>(binaryPublisher, trace);

    log.info("{} upload is starting", trace);
    final var result = uploading.upload(new UploadingCommand<>(command.id(), logged));
    log.info("{} upload started", trace);

    return result;
  }
}
