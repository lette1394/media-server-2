package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import org.reactivestreams.Publisher;

public interface BinaryPublisher<P extends Payload> extends Publisher<P> {
  long length();

  Attributes attributes();
}