package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import static java.time.Duration.ofSeconds;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;

import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublishers;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.internal.ThreadExecutorMap;
import io.vavr.control.Option;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public final class SpringWebFluxHttpClient implements HttpClient<DataBufferPayload> {
  private final BinaryPublishers<DataBufferPayload> binaryPublishers;
  private final Trace trace;

  @Override
  public CompletionStage<HttpResponse<DataBufferPayload>> get(GetRequest getRequest) {
    final var channels = new DefaultChannelGroup(ThreadExecutorMap.currentExecutor());
    final var client = reactor.netty.http.client.HttpClient.create()
      .doOnChannelInit((connectionObserver, channel, remoteAddress) -> {
      })
      .channelGroup(channels)
      .secure()
      .responseTimeout(ofSeconds(10));
    final var webClient = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(client))
      .build();

    return webClient
      .get()
      .uri(getRequest.url())
      .headers(toHeaders(getRequest))
      .retrieve()
      .toEntityFlux(toPayload())
      .map(entity -> {
        final var headers = entity.getHeaders().toSingleValueMap();
        final var publisher = Option.of(headers.get(CONTENT_LENGTH))
          .map(Long::parseLong)
          .map(length -> binaryPublishers.adapt(trace, entity.getBody(), length))
          .getOrElse(() -> binaryPublishers.adapt(trace, Mono.empty(), 0L));

        return new HttpResponse<>(entity.getStatusCode(), new HashMapHeaders(headers), publisher);
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
