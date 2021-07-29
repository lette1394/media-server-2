package com.github.lette1394.mediaserver2.core.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.config.infrastructure.AllResources;

public final class TestFixtures {
  private TestFixtures() {
  }

  private static final String CURRENT_PACKAGE = TestFixtures.class.getPackageName();

  public static final AllResources ALL_RESOURCES = AllResources.builder()
    .rootResourceDirectory("/core/config")
    .rootScanningPackage(CURRENT_PACKAGE)
    .objectMapper(new ObjectMapper(new YAMLFactory()).enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS))
    .build();
}
