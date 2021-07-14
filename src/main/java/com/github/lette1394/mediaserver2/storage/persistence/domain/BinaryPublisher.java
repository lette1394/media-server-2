package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public interface BinaryPublisher<P extends Payload> extends Publisher<P> {
  long length();

  static <P extends Payload> BinaryPublisher<P> adapt(long length, Publisher<? extends P> publisher) {
    return new BinaryPublisher<>() {
      @Override
      public long length() {
        return length;
      }

      @Override
      public void subscribe(Subscriber<? super P> subscriber) {
        publisher.subscribe(subscriber);
      }
    };
  }
}