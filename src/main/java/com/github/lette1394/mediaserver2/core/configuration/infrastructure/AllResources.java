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
  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;
  private final ObjectMapper objectMapper;

  private final ResourceScanner resourceScanner;
  private final SingleReloading singleReloading;
  private final MultiReloading multiReloading;

  private final Reloader reloader;

  public AllResources(String baseClassPath, String basePackage, ObjectMapper objectMapper) {
    this.fileResourcePathFactory = new FileResourcePathFactory(baseClassPath);
    this.reflections = new Reflections(basePackage);
    this.objectMapper = objectMapper;

    this.resourceScanner = resourceScanner();
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

  private ResourceScanner resourceScanner() {
    final var single = singleScanner();
    final var multi = multiScanner();
    final var merged = new Merging(single, multi);
    final var cached = new EagerCachingScanner(merged);

    return cached;
  }

  private SingleScanner singleScanner() {
    return new SingleScanner(fileResourcePathFactory, reflections);
  }

  private MultiScanner multiScanner() {
    return new MultiScanner(fileResourcePathFactory, reflections);
  }

  private Try<AllSingleResources> createSingle() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated = new SingleAnnotated(unsafeFileResources, fileResourcePathFactory);
        final var mapped = new SingleMapped(annotated, allMappedResourceTypes());
        final var cached = new SingleCache(mapped);
        final var warmed = new SingleWarmingUp(cached, singleScanner());

        return warmed;
      });
  }

  private Try<AllMultipleResources> createMulti() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated = new MultiAnnotated(unsafeFileResources, fileResourcePathFactory);
        final var mapped = new MultiMapped(annotated, allMappedResourceTypes());
        final var cached = new MultiCache(mapped);
        final var warmed = new MultiWarmingUp(cached, multiScanner());

        return warmed;
      });
  }

  private Try<FileResourceLoader> unsafeFileResources() {
    return Try.of(() -> {
      final var jackson = new Jackson(objectMapper);
      final var logged = new Logging(jackson);
      final var thread = new ThreadSafe(logged);
      final var cached = new Caching(thread);
      final var warmed = new WarmingUp(cached, resourceScanner);

      return warmed;
    });
  }

  private AllMappedResourceTypes allMappedResourceTypes() {
    return new ScanningCached(reflections);
  }
}
