package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class LoggingHeaderHttpClient<P extends Payload> implements HttpClient<P> {
  private final HttpClient<P> httpClient;
  private final Trace trace;

  @Override
  public CompletionStage<HttpResponse<P>> get(GetRequest getRequest) {
    log.info("{}> ########## request headers ##########", trace);
    log.info("{}> ########## local >> remote ##########", trace);
    log.info(toMultiLine(getRequest.headers().toMap()));
    log.info("{}> #####################################", trace);


    return httpClient
      .get(getRequest)
      .thenApply(response -> {
        log.info("{}> ########## response headers ##########", trace);
        log.info("{}> ########## local <<< remote ##########", trace);
        log.info(toMultiLine(response.headers().toMap()));
        log.info("{}> ######################################", trace);
        return response;
      });
  }

  private String toMultiLine(Map<String, String> map) {
    return map
      .entrySet()
      .stream()
      .sorted(Entry.comparingByKey())
      .map(entry -> format("%s> %s: %s", trace, entry.getKey(), entry.getValue()))
      .collect(joining(lineSeparator()));
  }
}

