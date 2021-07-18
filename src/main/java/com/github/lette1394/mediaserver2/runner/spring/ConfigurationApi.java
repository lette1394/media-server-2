package com.github.lette1394.mediaserver2.runner.spring;

import com.github.lette1394.mediaserver2.core.configuration.domain.Animal;
import com.github.lette1394.mediaserver2.core.configuration.domain.Person;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.AllResources;
import com.github.lette1394.mediaserver2.core.domain.TraceFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/configuration")
@RequiredArgsConstructor
public class ConfigurationApi {
  private final Reloader reloader;
  private final TraceFactory traceFactory;

  private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

  static {
    final var single = new AllResources().single();

    final var preloadedPerson = single.find(Person.class).get();
    final var preloadedAnimal = single.find(Animal.class).get();
    final var atomicLong = new AtomicLong();
    scheduledExecutorService.scheduleWithFixedDelay(() -> {
      single
        .find(Person.class)
        .peek(person -> System.out.printf("%s - %s%n", atomicLong.getAndIncrement(), person.hello()));
      System.out.printf("%s - %s[preloaded]%n", atomicLong.getAndIncrement(), preloadedPerson.hello());

      single
        .find(Animal.class)
        .peek(animal -> System.out.printf("%s - %s%n", atomicLong.getAndIncrement(), animal.type() + animal.name()));
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
