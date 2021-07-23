package com.github.lette1394.mediaserver2.runner.spring;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Animal;
import com.github.lette1394.mediaserver2.core.configuration.domain.Person;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import com.github.lette1394.mediaserver2.core.configuration.domain.Root;
import com.github.lette1394.mediaserver2.core.configuration.domain.Store;
import com.github.lette1394.mediaserver2.core.domain.TraceFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
  private final AllMultipleResources multi;

  private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

  public ConfigurationApi(
    Reloader reloader,
    TraceFactory traceFactory,
    AllSingleResources resources,
    AllMultipleResources multi) {

    this.reloader = reloader;
    this.traceFactory = traceFactory;
    this.resources = resources;
    this.multi = multi;

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

      try {
        final Root root = multi.find(Root.class, "0");
        System.out.println(root);


        final var store = resources.find(Store.class);
        System.out.println(store);
      } catch (Exception e) {
        System.out.println(e);
      }


    }, 0L, 1000L, TimeUnit.MILLISECONDS);
  }

  public static final ExecutorService executorService = Executors.newFixedThreadPool(32);

  @PutMapping("/reload")
  public Mono<ResponseEntity<?>> reload() {
    return Mono.fromCompletionStage(
      CompletableFuture
        .supplyAsync(() -> reloader.reload()
          .map(__ -> ok())
          .getOrElseGet(this::error), executorService)
    );
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
