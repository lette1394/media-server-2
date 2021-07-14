package com.github.lette1394.mediaserver2.storage.persistence.usecase;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;

public record UploadingCommand<P extends Payload>(
  Id id,
  BinaryPublisher<P> binaryPublisher) {
}
