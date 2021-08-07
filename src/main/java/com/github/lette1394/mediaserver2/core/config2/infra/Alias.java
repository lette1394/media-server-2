package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigTypeAlias;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Alias implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;

  @Override
  public <T> FileKey<T> create(Class<T> configType) {
    Option.of(configType.getDeclaredAnnotation(ConfigTypeAlias.class))
      .map(annotation -> (Class<T>)annotation.value())  // required
      .map(fileKeyFactory::create)
      .getOrElse(() -> fileKeyFactory.create(configType));

    return null;
  }

  @Override
  public <T> FileKey<T> create(Class<T> configType, String location) {
    return null;
  }
}
