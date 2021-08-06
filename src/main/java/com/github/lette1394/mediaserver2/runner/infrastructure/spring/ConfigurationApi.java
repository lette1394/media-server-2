package com.github.lette1394.mediaserver2.runner.infrastructure.spring;

import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/configuration")
public class ConfigurationApi {

  private final Reloader reloader;

  @PutMapping("/reload")
  public Mono<ResponseEntity<String>> reload(Trace trace) {
    log.info("{}> I'm in seoul", trace);
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
