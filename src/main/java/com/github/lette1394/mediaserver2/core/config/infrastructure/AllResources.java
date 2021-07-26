package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static java.time.Duration.ofSeconds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import io.vavr.control.Try;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;
import lombok.Builder;
import org.reflections.Reflections;

@SuppressWarnings({"UnnecessaryLocalVariable", "Convert2MethodRef"})
public final class AllResources implements Reloader {
  private final FileResourcePathFactory fileResourcePathFactory;
  private final Reflections reflections;
  private final FileResourceLoaders fileResourceLoaders;

  private final AllSingleResources single;
  private final AllMultipleResources multi;
  private final Reloader reloader;
  private final Warmer warmer;

  @Builder
  public AllResources(String rootResourceDirectory, String rootScanningPackage, ObjectMapper objectMapper) {
    final var jsonSchemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V7);
    final var fileResourcePath = FileResourcePath.create(rootResourceDirectory).get();
    final var executorService = new ForkJoinPool(4);

    this.fileResourcePathFactory = new FileResourcePathFactory(fileResourcePath);
    this.reflections = new Reflections(rootScanningPackage);
    this.warmer = new Warmer(executorService, ofSeconds(60));
    this.fileResourceLoaders = new FileResourceLoaders(objectMapper, warmer, jsonSchemaFactory, fileResourcePathFactory);

    final var singleReloading = new SingleReloading(() -> createSingle(), createSingle().get());
    final var multiReloading = new MultiReloading(() -> createMulti(), createMulti().get());
    final var sequence = new Sequence(List.of(singleReloading, multiReloading));
    final var async = new Async(sequence, executorService);

    this.reloader = async;
    this.single = new SingleAutoReloading(singleReloading);
    this.multi = new MultiAutoReloading(multiReloading);
  }

  public AllSingleResources single() {
    return single;
  }

  public AllMultipleResources multi() {
    return multi;
  }

  @Override
  public CompletionStage<? super Void> reload() {
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
