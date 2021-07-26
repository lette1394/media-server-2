package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.Payload;

public record HttpResponse<P extends Payload>(
  Headers headers,
  BinaryPublisher<P> binaryPublisher
) {
}
