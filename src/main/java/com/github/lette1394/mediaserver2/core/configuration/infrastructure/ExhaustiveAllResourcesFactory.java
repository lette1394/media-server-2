package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.infrastructure.FileExtensionAware.FileExtension;

public class ExhaustiveAllResourcesFactory {
  private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
    .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
    .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
    .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

  private final UnsafeFileResources resources;
  private final AllMappedResourceTypes scanned;

  public ExhaustiveAllResourcesFactory(String baseClassPath, String baseEntityScanningPackage) {
    var jackson = new JacksonLoader(objectMapper);
    var logged = new Slf4jLoggingAware(jackson);
    var yamlAware = new FileExtensionAware(FileExtension.YAML, logged);
    var baseAware = new BaseClassPathAware(new ClassPath(baseClassPath), yamlAware);
    var cached = new CachedLoader(baseAware);

    resources = cached;
    scanned = new TypeScanningCachedMappedResourceTypes(baseEntityScanningPackage);
  }

  public AllSingleResources single() {
    return new MappedSingleResources(new AnnotatedSingleFileResources(resources), scanned);
  }

  public AllMultipleResources multiple() {
    return new MappedMultipleResources(new AnnotatedMultipleFileResources(resources), scanned);
  }
}
