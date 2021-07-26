package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploader;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedUploader<P extends Payload> implements Uploader<P> {
  private final Uploader<P> uploader;
  private final Trace trace;

  @Override
  public CompletionStage<Void> upload(UploadingCommand<P> command) {
    final var newPublisher = new LoggedBinaryPublisher<>(command.binaryPublisher(), trace);
    final var newCommand = new UploadingCommand<>(command.id(), newPublisher);

    log.info("{} upload is starting", trace);
    final var result = uploader.upload(newCommand);
    log.info("{} upload started", trace);

    return result;
  }
}
