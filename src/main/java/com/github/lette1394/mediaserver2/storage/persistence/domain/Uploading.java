package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.usecase.UploadingCommand;
import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Uploading<P extends Payload> {
  CompletionStage<Void> upload(UploadingCommand<P> command);
}
