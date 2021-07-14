package com.github.lette1394.mediaserver2.core.infrastructure.http;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import com.github.lette1394.mediaserver2.core.domain.StringPayload;
import com.github.lette1394.mediaserver2.core.infrastructure.UuidTraceFactory;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class LoggingJsonBodyHttpClientTest {

  @Test
  void test() {
    final var trace = new UuidTraceFactory().create();
    final var httpClient = new HttpClient<>() {
      @Override
      public CompletionStage<GetResponse<Payload>> get(GetRequest getRequest) {
        final var map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("response-4", "4");
        map.put("response-2", "2");
        map.put("response-3", "3");
        map.put("response-1", "1");
        final var headers = new Headers(map);
        final var publisher = BinaryPublisher.adapt(0L, Mono.empty());
        final var response = new GetResponse<>(headers, publisher);

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

    loggedJson.get(new GetRequest(null, new Headers(map)))
      .thenAccept(response -> {
        System.out.println("done");
      });
  }

  @Test
  void test1() {
    final var trace = new UuidTraceFactory().create();
    final var httpClient = new HttpClient<StringPayload>() {
      @Override
      public CompletionStage<GetResponse<StringPayload>> get(GetRequest getRequest) {
        final var map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("header-4", "4");
        map.put("header-2", "2");
        map.put("header-3", "3");
        map.put("header-1", "1");
        final var headers = new Headers(map);
        final var publisher = BinaryPublisher.adapt(0L, Mono.just(new StringPayload("""
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
                    
          """)));
        final var response = new GetResponse<>(headers, publisher);

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

    loggedJson.get(new GetRequest(null, new Headers(map)))
      .thenAccept(response -> {
        System.out.println("done");
      });
  }
}