package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;

@SuppressWarnings("UnnecessaryLocalVariable")
public class ResourcesFactory {
  private static final ObjectMapper OBJECT_MAPPER = objectMapper();
  private static final String SCANNING_PACKAGE = "com.github.lette1394.mediaserver2.core.configuration.domain";

  public AllSingleResources singleResources() {
    final var annotated  = new SingleAnnotated(unsafeFileResources(), classPathFactory());
    final var mapped     = new SingleMapped(annotated, allMappedResourceTypes());
    final var autoReload = new SingleAutoReload(mapped);
    final var warmingUp  = new SingleWarmingUp(autoReload, SCANNING_PACKAGE);

    return warmingUp;
  }

  public AllMultipleResources multiResources() {
    final var annotated  = new MultiAnnotated(unsafeFileResources(), classPathFactory());
    final var mapped     = new MultiMapped(annotated, allMappedResourceTypes());
    final var autoReload = new MultiAutoReload(mapped);
    final var warmingUp  = new MultiWarmingUp(autoReload, SCANNING_PACKAGE, classPathFactory());

    return warmingUp;
  }

  private static UnsafeFileResources unsafeFileResources() {
    final var jackson    = new Jackson(OBJECT_MAPPER);
    final var logged     = new Logging(jackson);
    final var threadSafe = new ThreadSafe(logged);
    final var cached     = new Caching(threadSafe);

    return cached;
  }

  private static ClassPathFactory classPathFactory() {
    return new ClassPathFactory("/plugins");
  }

  private static AllMappedResourceTypes allMappedResourceTypes() {
    return new ScanningCached(SCANNING_PACKAGE);
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
