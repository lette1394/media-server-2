package com.github.lette1394.mediaserver2.runner.infrastructure.spring;

import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/configuration")
public class ConfigurationApi {

  private final Reloader reloader;

  @PutMapping("/reload")
  public Mono<ResponseEntity<String>> reload() {
    return Mono
      .fromCompletionStage(reloader.reload())
      .flatMap(__ -> ok())
      .onErrorResume(this::error);
  }

  private Mono<ResponseEntity<String>> ok() {
    return Mono.just(ResponseEntity
      .status(HttpStatus.OK)
      .build());
  }

  private Mono<ResponseEntity<String>> error(Throwable throwable) {
    return Mono.just(ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(throwable.toString()));
  }
}
