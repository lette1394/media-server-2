package com.github.lette1394.mediaserver2.runner.infrastructure.spring;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.trace.domain.TraceFactory;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.DataBufferPayload;
import com.github.lette1394.mediaserver2.storage.identification.domain.Id;
import com.github.lette1394.mediaserver2.core.stream.domain.BinaryPublishers;
import com.github.lette1394.mediaserver2.storage.persistence.domain.Uploaders;
import com.github.lette1394.mediaserver2.storage.persistence.domain.UploadingCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequestMapping("/")
@RequiredArgsConstructor
public class PersistenceApi {
  private final TraceFactory traceFactory;
  private final Uploaders<DataBufferPayload> uploaders;
  private final BinaryPublishers<DataBufferPayload> binaryPublishers;

  @PostMapping("/{key}")
  Mono<ResponseEntity<?>> uploading(@PathVariable String key, ServerWebExchange exchange) {
    final var request = exchange.getRequest();
    final var contentLength = request.getHeaders().getContentLength();
    requires(contentLength > 0, "content length > 0, but %s".formatted(contentLength));

    final var trace = traceFactory.create();
    final var id = new Id(key);
    final var publisher = binaryPublishers.adapt(trace, request.getBody().map(DataBufferPayload::new), contentLength);
    final var command = new UploadingCommand<>(id, publisher);

    final var result = uploaders.within(trace).upload(command);
    return Mono
      .fromCompletionStage(result)
      .map(__ -> ResponseEntity
        .status(HttpStatus.CREATED)
        .build());
  }
}
