package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
import java.util.List;
import org.reflections.Reflections;

@SuppressWarnings({"UnnecessaryLocalVariable", "Convert2MethodRef"})
public final class AllResources implements Reloader {
  private static final ClassPathFactory CLASS_PATH_FACTORY = new ClassPathFactory("/plugins");
  private static final Reflections REFLECTIONS = new Reflections("com.github.lette1394.mediaserver2");
  private static final ObjectMapper OBJECT_MAPPER = objectMapper();

  private static final Reloader SINGLE_RELOADING;
  private static final Reloader MULTI_RELOADING;

  private static final AllSingleResources ALL_SINGLE_RESOURCES;
  private static final AllMultipleResources ALL_MULTIPLE_RESOURCES;

  static {
    final var singleReloading = new SingleReloading(() -> createSingle(), createSingle().get());
    SINGLE_RELOADING = singleReloading;
    final var singleAutoReloading = new SingleAutoReloading(singleReloading);
    ALL_SINGLE_RESOURCES = singleAutoReloading;


    final var multiReloading = new MultiReloading(() -> createMulti(), createMulti().get());
    MULTI_RELOADING = multiReloading;
    final var multiAutoReloading = new MultiAutoReloading(multiReloading);
    ALL_MULTIPLE_RESOURCES = multiAutoReloading;
  }

  public AllSingleResources single() {
    return ALL_SINGLE_RESOURCES;
  }

  public AllMultipleResources multi() {
    return ALL_MULTIPLE_RESOURCES;
  }

  @Override
  public Try<Void> reload() {
    return Try
      .sequence(List.of(
        SINGLE_RELOADING.reload(),
        MULTI_RELOADING.reload()
      ))
      .map(__ -> null);
  }

  private static Try<AllSingleResources> createSingle() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated  = new SingleAnnotated(unsafeFileResources, CLASS_PATH_FACTORY);
        final var mapped     = new SingleMapped(annotated, allMappedResourceTypes());
        final var cached     = new SingleCache(mapped);
        final var warmingUp  = new SingleWarmingUp(cached, REFLECTIONS);

        return warmingUp;
      });
  }

  private static Try<AllMultipleResources> createMulti() {
    return unsafeFileResources()
      .map(unsafeFileResources -> {
        final var annotated  = new MultiAnnotated(unsafeFileResources, CLASS_PATH_FACTORY);
        final var mapped     = new MultiMapped(annotated, allMappedResourceTypes());
        final var cached     = new MultiCache(mapped);
        final var warmingUp  = new MultiWarmingUp(cached, REFLECTIONS, CLASS_PATH_FACTORY);

        return warmingUp;
      });
  }

  private static Try<UnsafeFileResources> unsafeFileResources() {
    return Try.of(() -> {
      final var jackson    = new Jackson(OBJECT_MAPPER);
      final var logged     = new Logging(jackson);
      final var threadSafe = new ThreadSafe(logged);
      final var cached     = new Caching(threadSafe);

      return cached;
    });
  }

  private static AllMappedResourceTypes allMappedResourceTypes() {
    return new ScanningCached(REFLECTIONS);
  }

  private static ObjectMapper objectMapper() {
    return new ObjectMapper(new YAMLFactory())
      .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
      .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
  }
}
