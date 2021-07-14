package com.github.lette1394.mediaserver2.core.infrastructure.http;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;

public record  GetResponse<P extends Payload>(
  Headers headers,
  BinaryPublisher<P> binaryPublisher
) {
}
