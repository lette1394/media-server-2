package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import com.github.lette1394.mediaserver2.core.stream.domain.AdaptedBinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.Attributes;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublisher;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublishers;
import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.stream.domain.StringPayload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.core.trace.infrastructure.UuidTraceFactory;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

class LoggingJsonBodyHttpClientTest {

  @Test
  void test() {
    final var trace = new UuidTraceFactory().newTrace();
    final var binaryPublishers = new BinaryPublishers<>() {
      @Override
      public BinaryPublisher<Payload> adapt(Trace trace, Publisher<Payload> publisher, long length) {
        return new AdaptedBinaryPublisher<>(publisher, Attributes.createEmpty(), length);
      }
    };
    final var httpClient = new HttpClient<>() {
      @Override
      public CompletionStage<HttpResponse<Payload>> get(GetRequest getRequest) {
        final var map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("response-4", "4");
        map.put("response-2", "2");
        map.put("response-3", "3");
        map.put("response-1", "1");
        final var headers = new HashMapHeaders(map);
        final var publisher = binaryPublishers.adapt(trace, Mono.empty(), 0L);
        final var response = new HttpResponse<>(HttpStatus.OK, headers, publisher);

        return CompletableFuture.completedFuture(response);
      }
    };

    final var loggedHeader = new LoggingHeaderHttpClient<>(httpClient, trace);
    final var loggedJson = new LoggingJsonBodyHttpClient<>(loggedHeader, trace);

    final var map = new HashMap<String, String>();
    map.put("Content-Type", "application/json");
    map.put("request-4", "4");
    map.put("request-2", "2");
    map.put("request-3", "3");
    map.put("request-1", "1");

    loggedJson.get(new GetRequest(null, new HashMapHeaders(map)))
      .thenAccept(response -> {
        System.out.println("done");
      });
  }

  @Test
  void test1() {
    final var trace = new UuidTraceFactory().newTrace();
    final var binaryPublishers = new BinaryPublishers<StringPayload>() {
      @Override
      public BinaryPublisher<StringPayload> adapt(Trace trace,
        Publisher<StringPayload> publisher,
        long length) {
        return new AdaptedBinaryPublisher<>(publisher, Attributes.createEmpty(), length);
      }
    };
    final var httpClient = new HttpClient<StringPayload>() {
      @Override
      public CompletionStage<HttpResponse<StringPayload>> get(GetRequest getRequest) {
        final var map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("header-4", "4");
        map.put("header-2", "2");
        map.put("header-3", "3");
        map.put("header-1", "1");
        final var headers = new HashMapHeaders(map);
        final var publisher = binaryPublishers.adapt(trace, Mono.just(new StringPayload("""
          {
              "glossary": {
                  "title": "example glossary",
          		"GlossDiv": {
                      "title": "S",
          			"GlossList": {
                          "GlossEntry": {
                              "ID": "SGML",
          					"SortAs": "SGML",
          					"GlossTerm": "Standard Generalized Markup Language",
          					"Acronym": "SGML",
          					"Abbrev": "ISO 8879:1986",
          					"GlossDef": {
                                  "para": "A meta-markup language, used to create markup languages such as DocBook.",
          						"GlossSeeAlso": ["GML", "XML"]
                              },
          					"GlossSee": "markup"
                          }
                      }
                  }
              }
          }
                    
          """)), 0L);
        final var response = new HttpResponse<>(HttpStatus.OK, headers, publisher);

        return CompletableFuture.completedFuture(response);
      }
    };

    final var loggedHeader = new LoggingHeaderHttpClient<>(httpClient, trace);
    final var loggedJson = new LoggingJsonBodyHttpClient<>(loggedHeader, trace);

    final var map = new HashMap<String, String>();
    map.put("Content-Type", "application/json");
    map.put("request-4", "a");
    map.put("request-2", "b");
    map.put("request-3", "c");
    map.put("request-1", "d");

    loggedJson.get(new GetRequest(null, new HashMapHeaders(map)))
      .thenAccept(response -> {
        System.out.println("done");
      });
  }
}