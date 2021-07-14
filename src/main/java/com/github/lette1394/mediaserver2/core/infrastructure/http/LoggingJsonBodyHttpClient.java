package com.github.lette1394.mediaserver2.core.infrastructure.http;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static reactor.core.scheduler.Schedulers.immediate;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.core.domain.Trace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
public class LoggingJsonBodyHttpClient<P extends Payload> implements HttpClient<P> {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private final HttpClient<P> httpClient;
  private final Trace trace;

  @Override
  public CompletionStage<GetResponse<P>> get(GetRequest getRequest) {
    return httpClient
      .get(getRequest)
      .thenApply(response -> {
        if (hasJsonBody(response)) {
          log(response);
        }
        return response;
      });
  }

  private Boolean hasJsonBody(GetResponse<P> response) {
    return response.headers()
      .value(HttpHeaders.CONTENT_TYPE)
      .map(MediaType::parseMediaType)
      .map(contentType -> contentType.equalsTypeAndSubtype(MediaType.APPLICATION_JSON))
      .orElse(false);
  }

  private void log(GetResponse<P> response) {
    final var builder = new StringBuilder();
    final var publisher = response.binaryPublisher();

    Flux.from(publisher)
      .doOnNext(payload -> builder.append(new String(payload.toBytes())))
      .doOnComplete(() -> {
        final var json = builder.toString();
        log.info("{}> ########## json body ##########", trace);
        log.info(toPretty(json));
        log.info("{}> ###############################", trace);
      })
      .subscribeOn(immediate())
      .subscribe();
  }

  private String toPretty(String json) {
    return Try.of(() -> JsonParser.parseString(json))
      .map(GSON::toJson)
      .map(raw -> Arrays
        .stream(raw.split(lineSeparator()))
        .map(line -> format("%s> %s", trace, line))
        .collect(joining(lineSeparator(), lineSeparator(), "")))
      .getOrElse(() -> format("illegal json format: %s", json));
  }
}

