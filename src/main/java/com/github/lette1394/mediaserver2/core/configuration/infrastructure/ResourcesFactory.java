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
    final var annotated = new AnnotatedSingleFileResources(unsafeFileResources(), classPathFactory());
    final var mapped = new MappedSingleResources(annotated, allMappedResourceTypes());
    final var single = new AutoReloadSingleResource(mapped);
    final var warmedUp = new WarmUpFileLoader(single, SCANNING_PACKAGE);

    return warmedUp;
  }

  public AllMultipleResources multiResources() {
    final var annotated = new AnnotatedMultipleFileResources(unsafeFileResources(), classPathFactory());
    final var mapped = new MappedMultipleResources(annotated, allMappedResourceTypes());
    final var multi = new AutoReloadMultiResource(mapped);
    final var warmedUp = new WarmUpFileMultiLoader(multi, SCANNING_PACKAGE, classPathFactory());

    return warmedUp;
  }

  private static UnsafeFileResources unsafeFileResources() {
    final var jackson = new JacksonFileLoader(OBJECT_MAPPER);
    final var logged = new Slf4jLoggingAware(jackson);
    final var threadSafe = new ThreadSafeLoader(logged);
    final var cached = new CachedLoader(threadSafe);

    return cached;
  }

  private static ClassPathFactory classPathFactory() {
    return new ClassPathFactory("/plugins");
  }

  private static AllMappedResourceTypes allMappedResourceTypes() {
    return new TypeScanningCachedMappedResourceTypes(SCANNING_PACKAGE);
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
