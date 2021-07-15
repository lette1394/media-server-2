package com.github.lette1394.mediaserver2.core.infrastructure.http;

import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;

import com.github.lette1394.mediaserver2.core.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.storage.persistence.domain.BinaryPublisher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.internal.ThreadExecutorMap;
import io.vavr.control.Option;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SpringWebFluxHttpClient implements HttpClient<DataBufferPayload> {
  @Override
  public CompletionStage<GetResponse<DataBufferPayload>> get(GetRequest getRequest) {
    final var channels = new DefaultChannelGroup("", ThreadExecutorMap.currentExecutor());
    final var client = reactor.netty.http.client.HttpClient.create()
      .channelGroup(channels)
      .secure()
      .responseTimeout(Duration.ofSeconds(10));

    final var webClient = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(client))
      .build();
    return webClient
      .get()
      .uri(getRequest.url().toString())
      .headers(toHeaders(getRequest))
      .retrieve()
      .toEntityFlux(toPayload())
      .map(entity -> {
        final var headers = entity.getHeaders().toSingleValueMap();
        final var publisher = Option.of(headers.get(CONTENT_LENGTH))
          .map(Long::parseLong)
          .map(length -> BinaryPublisher.adapt(length, entity.getBody()))
          .getOrElse(() -> BinaryPublisher.adapt(0L, Mono.empty()));

        return new GetResponse<>(new Headers(headers), publisher);
      })
      .toFuture();
  }

  private Consumer<HttpHeaders> toHeaders(GetRequest getRequest) {
    return httpHeaders -> getRequest
      .headers()
      .toMap()
      .forEach((key, value) -> httpHeaders.put(key, List.of(value)));
  }

  private BodyExtractor<Flux<DataBufferPayload>, ClientHttpResponse> toPayload() {
    return (inputMessage, __) -> inputMessage.getBody().map(DataBufferPayload::new);
  }
}
