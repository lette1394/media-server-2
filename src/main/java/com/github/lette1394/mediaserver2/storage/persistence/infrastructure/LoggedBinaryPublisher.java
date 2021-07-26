package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.stream.domain.Attributes;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
public class LoggedBinaryPublisher<P extends Payload> implements BinaryPublisher<P> {
  private final BinaryPublisher<P> binaryPublisher;
  private final Trace trace;

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
    binaryPublisher.subscribe(new LoggedSubscriber<>(subscriber, trace));
  }
}
