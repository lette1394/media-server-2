package com.github.lette1394.mediaserver2.core.config.infrastructure;

import static com.github.lette1394.mediaserver2.core.config.infrastructure.ClassPathFile.createClassPathFile;
import static com.github.lette1394.mediaserver2.core.config.infrastructure.EagerCachingScanner.createEagerCachingScanner;
import static com.github.lette1394.mediaserver2.core.config.infrastructure.MultiWarmingUp.createMultiWarmingUp;
import static com.github.lette1394.mediaserver2.core.config.infrastructure.SingleWarmingUp.createSingleWarmingUp;
import static java.time.Duration.ofSeconds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
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
  private final Warmer warmer;
  private final Reflections reflections;
  private final ClassPathFileFactory classPathFileFactory;
  private final DeserializerFactory deserializerFactory;

  private final AllSingleConfigs single;
  private final AllMultiConfigs multi;
  private final Reloader reloader;

  @Builder
  public AllResources(String rootResourceDirectory, String rootScanningPackage,
    ObjectMapper objectMapper) {
    final var jsonSchemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V7);
    final var fileResourcePath = createClassPathFile(rootResourceDirectory).get();
    final var executorService = new ForkJoinPool(4);

    this.warmer = new Warmer(executorService, ofSeconds(60));
    this.reflections = new Reflections(rootScanningPackage);
    this.classPathFileFactory = new ClassPathFileFactory(fileResourcePath);
    this.deserializerFactory = new DeserializerFactory(objectMapper, warmer, jsonSchemaFactory, classPathFileFactory);

    final var singleReloading = new SingleReloading(() -> createSingle(), createSingle().get());
    final var multiReloading = new MultiReloading(() -> createMulti(), createMulti().get());
    final var sequence = new Sequence(List.of(singleReloading, multiReloading));
    final var async = new Async(sequence, executorService);

    this.reloader = async;
    this.single = new SingleAutoReloading(singleReloading);
    this.multi = new MultiAutoReloading(multiReloading);
  }

  public AllSingleConfigs single() {
    return single;
  }

  public AllMultiConfigs multi() {
    return multi;
  }

  @Override
  public CompletionStage<? super Void> reload() {
    return reloader.reload();
  }

  private Try<AllSingleConfigs> createSingle() {
    return createSingleScanner().flatMap(
      scanner -> deserializerFactory.create(scanner).flatMap(
        loader -> {
          final var annotated = new SingleAnnotated(loader, classPathFileFactory);
          final var aliased = new SingleTypeAlias(annotated, createAllTypeAliases());
          final var cached = new SingleCache(aliased);
          final var warmed = createSingleWarmingUp(cached, scanner, warmer);

          return warmed;
        }));
  }

  private Try<AllMultiConfigs> createMulti() {
    return createMultiScanner().flatMap(
      scanner -> deserializerFactory.create(scanner).flatMap(
        loader -> {
          final var annotated = new MultiAnnotated(loader, classPathFileFactory);
          final var aliased = new MultiTypeAlias(annotated, createAllTypeAliases());
          final var cached = new MultiCache(aliased);
          final var warmed = createMultiWarmingUp(cached, scanner, warmer);

          return warmed;
        }));
  }

  private Try<ResourceScanner> createSingleScanner() {
    final var scanner = new SingleScanner(classPathFileFactory, reflections);
    final var cached = createEagerCachingScanner(scanner);
    return cached;
  }

  private Try<ResourceScanner> createMultiScanner() {
    final var scanner = new MultiScanner(classPathFileFactory, reflections);
    final var cached = createEagerCachingScanner(scanner);
    return cached;
  }

  private AllTypeAliases createAllTypeAliases() {
    return new ScanningCached(reflections);
  }
}
