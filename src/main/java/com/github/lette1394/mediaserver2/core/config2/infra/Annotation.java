package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;

import com.github.lette1394.mediaserver2.core.config2.domain.Config;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigLocation;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Annotation implements FileKeyFactory {

  @Override
  public <T> FileKey<T> singleKey(Class<T> configType) {
    requireConfig(configType);
    requires(configLocations(configType).size() == 1, () -> new ConfigException("""
      config class should have at exactly one @ConfigLocation annotation"""));

    final var nameKeyMap = toNameKeyMap(configType);
    requires(nameKeyMap.size() == 1);

    return nameKeyMap.values().iterator().next();
  }

  @Override
  public <T> Map<String, FileKey<T>> multiKey(Class<T> configType) {
    requireConfig(configType);
    requires(configLocations(configType).size() > 0, () -> new ConfigException("""
      config class should have at least one @ConfigLocation annotation"""));

    return toNameKeyMap(configType);
  }

  private static <T> void requireConfig(Class<T> configType) {
    final var configAnnotation = configType.getDeclaredAnnotation(Config.class);
    requires(configAnnotation != null, () -> new ConfigException("""
      config class should have @Config annotation"""));
  }

  private static <T> Map<String, FileKey<T>> toNameKeyMap(Class<T> configType) {
    return configLocations(configType)
      .stream()
      .map(ConfigLocation::value)
      .flatMap(location -> FileKey.scan(configType, location).stream())
      .collect(toMap(fileKey -> fileKey.path().toFile().getName(), Function.identity()));
  }

  private static List<ConfigLocation> configLocations(Class<?> configType) {
    return List.of(configType.getDeclaredAnnotationsByType(ConfigLocation.class));
  }
}
