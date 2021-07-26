package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Uploader<P extends Payload> {
  CompletionStage<Void> upload(UploadingCommand<P> command);
}
