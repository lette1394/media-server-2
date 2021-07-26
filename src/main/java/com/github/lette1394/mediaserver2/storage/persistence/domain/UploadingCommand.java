package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;

public record UploadingCommand<P extends Payload>(
  Id id,
  BinaryPublisher<P> binaryPublisher) {
}
