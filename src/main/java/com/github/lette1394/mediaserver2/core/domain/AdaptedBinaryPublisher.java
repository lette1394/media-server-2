package com.github.lette1394.mediaserver2.core.domain;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
public class AdaptedBinaryPublisher<P extends Payload> implements BinaryPublisher<P> {
  private final Publisher<P> publisher;
  private final long length;

  private final Attributes attributes = Attributes.empty();

  @Override
  public long length() {
    return length;
  }

  @Override
  public Attributes attributes() {
    return attributes;
  }

  @Override
  public void subscribe(Subscriber<? super P> subscribe) {
    publisher.subscribe(subscribe);
  }
}