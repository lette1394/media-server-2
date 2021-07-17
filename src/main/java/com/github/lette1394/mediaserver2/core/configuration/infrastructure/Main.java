package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.Person;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.ExhaustiveAllResourcesFactory;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.FileExtensionAware.FileExtension;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    final var objectMapper = new ObjectMapper(new YAMLFactory())
      .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
      .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

    final var jackson = new JacksonFileLoader(objectMapper);
    final var logged = new Slf4jLoggingAware(jackson);
    final var yamlAware = new FileExtensionAware(FileExtension.YAML, logged);
    final var basePathAware = new BaseClassPathAware(new ClassPath("/plugins"), yamlAware);
    final var cached = new CachedLoader(basePathAware);
    final var scanned = new TypeScanningCachedMappedResourceTypes("com.github.lette1394.mediaserver2.core.configuration.domain");

    final var annotated = new AnnotatedSingleFileResources(cached);
    final var mapped = new MappedSingleResources(annotated, scanned);
    final var single = new AutoReloadSingleResource(mapped);
    final var people = single.find(Person.class).get();


    final var executor = Executors.newScheduledThreadPool(4);
    final var evictor = Executors.newScheduledThreadPool(1);
    evictor.scheduleWithFixedDelay(() -> {
      cached.evict();
    }, 0, 3000, TimeUnit.MILLISECONDS);

    executor.scheduleWithFixedDelay(() -> {
      System.out.println(people.isKim());
      System.out.println(people.hello());
    }, 0, 1000, TimeUnit.MILLISECONDS);
  }
}
