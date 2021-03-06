package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.config2.infra.FileKey.scanFileKey;
import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.github.lette1394.mediaserver2.core.config2.domain.Config;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigLocation;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Annotated implements FileKeyFactory {

  @Override
  public <T> FileKey<T> singleKey(Class<T> configType) {
    requireConfig(configType);
    requires(configLocations(configType).size() == 1, () -> new ConfigException("""
      config class should have at exactly one @ConfigLocation annotation"""));

    final var nameKeyMap = allFileKeys(configType);
    requires(nameKeyMap.size() == 1);
    return nameKeyMap.iterator().next();
  }

  @Override
  public <T> MultiFileKey<T> multiKey(Class<T> configType) {
    requireConfig(configType);
    requires(configLocations(configType).size() > 0, () -> new ConfigException("""
      config class should have at least one @ConfigLocation annotation"""));

    return new MultiFileKey<>(allFileKeys(configType));
  }

  private static <T> void requireConfig(Class<T> configType) {
    final var configAnnotation = configType.getDeclaredAnnotation(Config.class);
    requires(configAnnotation != null, () -> new ConfigException("""
      config class should have @Config annotation"""));
  }

  private static <T> Set<FileKey<T>> allFileKeys(Class<T> configType) {
    return configLocations(configType)
      .stream()
      .map(ConfigLocation::value)
      .flatMap(location -> scanFileKey(configType, location).stream())
      .collect(toUnmodifiableSet());
  }

  private static List<ConfigLocation> configLocations(Class<?> configType) {
    return List.of(configType.getDeclaredAnnotationsByType(ConfigLocation.class));
  }
}
