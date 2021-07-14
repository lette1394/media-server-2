package com.github.lette1394.mediaserver2.storage.domain;

import static java.lang.String.format;

import com.github.lette1394.mediaserver2.core.domain.DelegatingSubscriber;
import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import org.reactivestreams.Subscriber;

public class BrokenSubscriber<P extends Payload> extends DelegatingSubscriber<P> {
  private final BinaryPublisher<? extends Payload> binaryPublisher;
  private final long exceptionAt;

  private long position;
  private boolean triggered;

  public BrokenSubscriber(
    BinaryPublisher<? extends Payload> binaryPublisher,
    Subscriber<? super P> subscriber,
    long exceptionAt) {

    super(subscriber);
    this.binaryPublisher = binaryPublisher;
    this.exceptionAt = exceptionAt;
  }

  @Override
  public void onNext(P item) {
    if (triggered) {
      return;
    }
    if (position >= exceptionAt) {
      triggered = true;
      onError(new BrokenIOException(format(
        "broken read triggered, size:[%s], exceptionAt:[%s]",
        binaryPublisher.length(),
        exceptionAt)));
      return;
    }

    position += item.length();
    super.onNext(item);
  }

  @Override
  public void onComplete() {
    if (triggered) {
      return;
    }
    super.onComplete();
  }
}