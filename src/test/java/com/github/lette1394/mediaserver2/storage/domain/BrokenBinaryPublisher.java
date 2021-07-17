package com.github.lette1394.mediaserver2.storage.domain;

import static com.github.lette1394.mediaserver2.core.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Attributes;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import org.reactivestreams.Subscriber;

public class BrokenBinaryPublisher<P extends Payload> implements BinaryPublisher<P> {
  private final BinaryPublisher<P> binaryPublisher;
  private final long exceptionAt;

  public BrokenBinaryPublisher(
    BinaryPublisher<P> binaryPublisher,
    long exceptionAt) {

    requires(exceptionAt >= 0, "exceptionAt >= 0");
    requires(exceptionAt < binaryPublisher.length(), "exceptionAt < totalLength");

    this.binaryPublisher = binaryPublisher;
    this.exceptionAt = exceptionAt;
  }

  @Override
  public void subscribe(Subscriber<? super P> subscriber) {
    binaryPublisher.subscribe(new BrokenSubscriber<>(binaryPublisher, subscriber, exceptionAt));
  }

  @Override
  public long length() {
    return binaryPublisher.length();
  }

  @Override
  public Attributes attributes() {
    return binaryPublisher.attributes();
  }
}
