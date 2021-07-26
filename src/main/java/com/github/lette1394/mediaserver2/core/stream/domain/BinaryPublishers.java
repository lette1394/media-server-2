package com.github.lette1394.mediaserver2.core.stream.domain;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import org.reactivestreams.Publisher;

@FunctionalInterface
public interface BinaryPublishers<P extends Payload> {
  BinaryPublisher<P> adapt(Trace trace, Publisher<P> publisher, long length);
}
