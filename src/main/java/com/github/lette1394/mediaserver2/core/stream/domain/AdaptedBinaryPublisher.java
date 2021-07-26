package com.github.lette1394.mediaserver2.core.stream.domain;

import static com.github.lette1394.mediaserver2.core.stream.domain.Contracts.requires;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class AdaptedBinaryPublisher<P extends Payload> implements BinaryPublisher<P> {
  private final Publisher<P> publisher;
  private final Attributes attributes;
  private final long length;

  public AdaptedBinaryPublisher(Publisher<P> publisher, Attributes attributes, long length) {
    requires(length >= 0, "positive or zero length required");

    this.publisher = publisher;
    this.attributes = attributes;
    this.length = length;
  }

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