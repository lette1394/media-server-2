package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.time.Duration.ofSeconds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import io.vavr.control.Try;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import org.reflections.Reflections;

@SuppressWarnings({"UnnecessaryLocalVariable", "Convert2MethodRef"})
public final class AllResources implements Reloader {
  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;
  private final FileResourceLoaders fileResourceLoaders;

  private final SingleReloading singleReloading;
  private final MultiReloading multiReloading;
  private final Reloader reloader;
  private final Warmer warmer;

  public AllResources(String baseClassPath, String basePackage, ObjectMapper objectMapper) {
    final var jsonSchemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V7);

    this.fileResourcePathFactory = new FileResourcePathFactory(FileResourcePath.create(baseClassPath).get());
    this.reflections = new Reflections(basePackage);
    this.warmer = new Warmer(new ForkJoinPool(4), ofSeconds(60));
    this.fileResourceLoaders = new FileResourceLoaders(objectMapper, warmer, jsonSchemaFactory, fileResourcePathFactory);

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
    return singleScanner().flatMap(
      scanner -> fileResourceLoaders.create(scanner).flatMap(
        loader -> {
          final var annotated = new SingleAnnotated(loader, fileResourcePathFactory);
          final var mapped = new SingleMapped(annotated, allMappedResourceTypes());
          final var cached = new SingleCache(mapped);
          final var warmed = SingleWarmingUp.create(cached, scanner, warmer);

          return warmed;
        }));
  }

  private Try<AllMultipleResources> createMulti() {
    return multiScanner().flatMap(
      scanner -> fileResourceLoaders.create(scanner).flatMap(
        loader -> {
          final var annotated = new MultiAnnotated(loader, fileResourcePathFactory);
          final var mapped = new MultiMapped(annotated, allMappedResourceTypes());
          final var cached = new MultiCache(mapped);
          final var warmed = MultiWarmingUp.create(cached, scanner, warmer);

          return warmed;
        }));
  }

  private Try<ResourceScanner> singleScanner() {
    final var scanner = new SingleScanner(fileResourcePathFactory, reflections);
    final var cached = EagerCachingScanner.create(scanner);
    return cached;
  }

  private Try<ResourceScanner> multiScanner() {
    final var scanner = new MultiScanner(fileResourcePathFactory, reflections);
    final var cached = EagerCachingScanner.create(scanner);
    return cached;
  }

  private AllMappedResourceTypes allMappedResourceTypes() {
    return new ScanningCached(reflections);
  }
}
