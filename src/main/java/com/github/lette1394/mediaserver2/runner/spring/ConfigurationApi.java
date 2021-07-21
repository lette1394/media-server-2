package com.github.lette1394.mediaserver2.runner.spring;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Animal;
import com.github.lette1394.mediaserver2.core.configuration.domain.Person;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import com.github.lette1394.mediaserver2.core.domain.TraceFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/configuration")
public class ConfigurationApi {
  private final Reloader reloader;
  private final TraceFactory traceFactory;
  private final AllSingleResources resources;

  private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

  public ConfigurationApi(
    Reloader reloader,
    TraceFactory traceFactory,
    AllSingleResources resources) {

    this.reloader = reloader;
    this.traceFactory = traceFactory;
    this.resources = resources;

    final var preloadedPerson = this.resources.find(Person.class);
    final var preloadedAnimal = this.resources.find(Animal.class);
    final var atomicLong = new AtomicLong();

    scheduledExecutorService.scheduleWithFixedDelay(() -> {
      final var person = this.resources.find(Person.class);
      System.out.printf("%s - %s%n", atomicLong.getAndIncrement(), person.hello());
      System.out.printf("%s - %s[preloaded]%n", atomicLong.getAndIncrement(), preloadedPerson.hello());

      final var animal = this.resources.find(Animal.class);
      System.out.printf("%s - %s%n", atomicLong.getAndIncrement(), animal.type() + animal.name());
      System.out.printf("%s - %s[preloaded]%n", atomicLong.getAndIncrement(), preloadedAnimal.type() + preloadedAnimal.name());



    }, 0L, 1000L, TimeUnit.MILLISECONDS);
  }

  @PutMapping("/reload")
  public Mono<ResponseEntity<?>> reload() {
    final var reload = reloader.reload();

    return Mono.just(reload
      .map(__ -> ok())
      .getOrElseGet(this::error));
  }

  private ResponseEntity<Object> ok() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .build();
  }

  private ResponseEntity<Object> error(Throwable throwable) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(throwable.toString());
  }
}
