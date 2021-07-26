package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;

public record  GetResponse<P extends Payload>(
  Headers headers,
  BinaryPublisher<P> binaryPublisher
) {
}
