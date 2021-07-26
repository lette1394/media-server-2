package com.github.lette1394.mediaserver2.storage.persistence.infrastructure;

import com.github.lette1394.mediaserver2.core.stream.domain.DelegatingSubscriber;
import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;

@Slf4j
class LoggedSubscriber<P extends Payload> extends DelegatingSubscriber<P> {
  private final Trace trace;

  public LoggedSubscriber(Subscriber<? super P> subscriber, Trace trace) {
    super(subscriber);
    this.trace = trace;
  }

  @Override
  public void onError(Throwable throwable) {
    super.onError(throwable);
    log.info("{} got error: {}", trace, throwable);
  }

  @Override
  public void onComplete() {
    super.onComplete();
    log.info("{} done", trace);
  }
}
