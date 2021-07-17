package com.github.lette1394.mediaserver2.storage.persistence.domain;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.core.domain.Trace;
import org.reactivestreams.Publisher;

@FunctionalInterface
public interface BinaryPublishers<P extends Payload> {
  BinaryPublisher<P> adapt(Trace trace, Publisher<P> publisher, long length);
}
