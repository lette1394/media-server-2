package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.github.lette1394.mediaserver2.core.config2.domain.Config;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigLocation;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Annotation implements FileKeyFactory {
  @Override
  public <T> FileKey<T> create(Class<T> configType) {
    final var configAnnotation = configType.getDeclaredAnnotation(Config.class);
    requires(configAnnotation != null, () -> new ConfigException("""
      config class should have @Config annotation"""));

    final var configLocations = configLocations(configType);
    requires(configLocations.size() == 1, () -> new ConfigException("""
      config class should have at exactly one @ConfigLocation annotation"""));

    final var fileKeys = configLocations
      .stream()
      .map(ConfigLocation::value)
      .flatMap(location -> FileKey.scan(configType, location).stream())
      .collect(toUnmodifiableSet());
    requires(fileKeys.size() == 1);

    return fileKeys.iterator().next();
  }

  @Override
  public <T> FileKey<T> create(Class<T> configType, String name) {
    final var configAnnotation = configType.getDeclaredAnnotation(Config.class);
    requires(configAnnotation != null, () -> new ConfigException("""
      config class should have @Config annotation"""));

    final var configLocations = configLocations(configType);
    requires(configLocations.size() > 0, () -> new ConfigException("""
      config class should have at least one @ConfigLocation annotation"""));

    final var nameFileKeyMap = configLocations
      .stream()
      .map(ConfigLocation::value)
      .flatMap(location -> FileKey.scan(configType, location).stream())
      .collect(toMap(fileKey -> fileKey.path().toFile().getName(), Function.identity()));
    requires(nameFileKeyMap.containsKey(name), () -> new ConfigException("unknown config name: [%s]".formatted(name)));

    return nameFileKeyMap.get(name);
  }

  private static List<ConfigLocation> configLocations(Class<?> configType) {
    return List.of(configType.getDeclaredAnnotationsByType(ConfigLocation.class));
  }
}
