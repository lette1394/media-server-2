package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.Uploading;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedUploading implements Uploading {
  private final Uploading uploading;
  private final Trace trace;

  @Override
  public <P extends Payload> CompletionStage<Void> upload(UploadingCommand<P> command) {
    final var binaryPublisher = command.binaryPublisher();
    final var logged = new LoggedBinaryPublisher<>(binaryPublisher, trace);

    log.info("{} upload is starting", trace);
    final var result = uploading.upload(new UploadingCommand<>(command.id(), logged));
    log.info("{} upload started", trace);

    return result;
  }

}
