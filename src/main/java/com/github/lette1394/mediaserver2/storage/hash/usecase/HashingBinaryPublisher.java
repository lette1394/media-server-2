package com.github.lette1394.mediaserver2.storage.hash.usecase;

import com.github.lette1394.mediaserver2.core.domain.DelegatingSubscriber;
import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.storage.hash.domain.Hasher;
import com.github.lette1394.mediaserver2.core.domain.Attribute;
import com.github.lette1394.mediaserver2.core.domain.Attributes;
import com.github.lette1394.mediaserver2.core.domain.BinaryPublisher;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
public class HashingBinaryPublisher<P extends Payload> implements BinaryPublisher<P> {
  public static final Attribute<Hasher> HASHER_ATTRIBUTE = new Attribute<>(
    "com.github.lette1394.mediaserver2.storage.hash.usecase.HashingBinaryPublisher.HASHER_ATTRIBUTE");

  private final BinaryPublisher<P> binaryPublisher;
  private final Hasher hasher;

  @Override
  public long length() {
    return binaryPublisher.length();
  }

  @Override
  public Attributes attributes() {
    return binaryPublisher.attributes();
  }

  @Override
  public void subscribe(Subscriber<? super P> subscriber) {
    binaryPublisher.attributes().putAttribute(HASHER_ATTRIBUTE, hasher);
    binaryPublisher.subscribe(new HashingSubscriber<>(subscriber, hasher));
  }

  private static class HashingSubscriber<P extends Payload> extends DelegatingSubscriber<P> {
    private final Hasher hasher;

    public HashingSubscriber(Subscriber<? super P> subscriber, Hasher hasher) {
      super(subscriber);
      this.hasher = hasher;
    }

    @Override
    public void onNext(P item) {
      hasher.update(item.toBytes());
      super.onNext(item);
    }
  }
}

