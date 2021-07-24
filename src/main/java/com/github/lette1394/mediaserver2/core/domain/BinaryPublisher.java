package com.github.lette1394.mediaserver2.core.domain;

import org.reactivestreams.Publisher;

public interface BinaryPublisher<P extends Payload> extends Publisher<P> {
  long length();

  Attributes attributes();
}