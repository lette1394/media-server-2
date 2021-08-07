package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigTypeAlias;
import io.vavr.control.Option;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Alias implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;

  @Override
  public <T> FileKey<T> singleKey(Class<T> configType) {
    Option.of(configType.getDeclaredAnnotation(ConfigTypeAlias.class))
      .map(annotation -> (Class<T>)annotation.value())  // required
      .map(fileKeyFactory::singleKey)
      .getOrElse(() -> fileKeyFactory.singleKey(configType));

    return null;
  }

  @Override
  public <T> Map<String, FileKey<T>> multiKey(Class<T> configType) {
    return null;
  }
}
