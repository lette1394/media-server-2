package com.github.lette1394.mediaserver2.core.config2.infra;

import static com.github.lette1394.mediaserver2.core.fluency.domain.Contracts.requires;

import com.github.lette1394.mediaserver2.core.config2.domain.Config;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigException;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigLocation;
import com.github.lette1394.mediaserver2.core.fluency.domain.Contracts;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Annotation implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;

  @Override
  public <T> FileKey<T> create(Class<T> configType) {
    final var configAnnotation = configType.getDeclaredAnnotation(Config.class);
    requires(configAnnotation != null, () -> new ConfigException("""
      config class should have @Config annotation"""));

    final var configLocations = configType.getDeclaredAnnotationsByType(ConfigLocation.class);
    requires(configLocations.length > 0, () -> new ConfigException("""
      config class should have at least one @ConfigLocation annotation"""));

    Option.of(configType.getAnnotation(ConfigLocation.class))
      .map(ConfigLocation::value);  // required
//      .map(keyFactory::create)
//      .getOrElse(() -> keyFactory.create(deserializedType));

    return null;
  }

  @Override
  public <T> FileKey<T> create(Class<T> configType, String name) {
    return null;
  }
}
