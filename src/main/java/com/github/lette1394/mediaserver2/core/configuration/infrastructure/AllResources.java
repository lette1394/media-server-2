package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
import java.util.List;
import org.reflections.Reflections;

@SuppressWarnings({"UnnecessaryLocalVariable", "Convert2MethodRef"})
public final class AllResources implements Reloader {
  private final ClassPathFactory classPathFactory;
  private final Reflections reflections;
  private final ObjectMapper objectMapper;

  private final Reloader reloader;

  private final SingleReloading singleReloading;
  private final MultiReloading multiReloading;

  public AllResources(String baseClassPath, String basePackage, ObjectMapper objectMapper) {
    this.classPathFactory = new ClassPathFactory(baseClassPath);
    this.reflections = new Reflections(basePackage);
    this.objectMapper = objectMapper;

    this.singleReloading = new SingleReloading(() -> createSingle(), createSingle().get());
    this.multiReloading = new MultiReloading(() -> createMulti(), createMulti().get());

    this.reloader = new Sequence(List.of(singleReloading, multiReloading));
  }

  public AllSingleResources single() {
    return new SingleAutoReloading(singleReloading);
  }

  public AllMultipleResources multi() {
    return new MultiAutoReloading(multiReloading);
  }

  @Override
  public Try<Void> reload() {
    return reloader.reload();
  }

  private Try<AllSingleResources> createSingle() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated = new SingleAnnotated(unsafeFileResources, classPathFactory);
        final var mapped = new SingleMapped(annotated, allMappedResourceTypes());
        final var cached = new SingleCache(mapped);
        final var warmed = new SingleWarmingUp(cached, reflections);

        return warmed;
      });
  }

  private Try<AllMultipleResources> createMulti() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated = new MultiAnnotated(unsafeFileResources, classPathFactory);
        final var mapped = new MultiMapped(annotated, allMappedResourceTypes());
        final var cached = new MultiCache(mapped);
        final var warmed = new MultiWarmingUp(cached, reflections, classPathFactory);

        return warmed;
      });
  }

  private Try<UnsafeFileResources> unsafeFileResources() {
    return Try.of(() -> {
      final var jackson = new Jackson(objectMapper);
      final var logged = new Logging(jackson);
      final var thread = new ThreadSafe(logged);
      final var cached = new Caching(thread);
      final var warmed = new WarmingUp(cached, reflections, classPathFactory);

      return warmed;
    });
  }

  private AllMappedResourceTypes allMappedResourceTypes() {
    return new ScanningCached(reflections);
  }
}
