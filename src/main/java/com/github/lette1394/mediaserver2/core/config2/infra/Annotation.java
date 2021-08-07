package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.ConfigLocation;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Annotation implements FileKeyFactory {
  private final FileKeyFactory fileKeyFactory;

  @Override
  public <T> FileKey<T> create(Class<T> type) {
    Option.of(type.getAnnotation(ConfigLocation.class))
      .map(ConfigLocation::value);  // required
//      .map(keyFactory::create)
//      .getOrElse(() -> keyFactory.create(type));

    return null;
  }

  @Override
  public <T> FileKey<T> create(Class<T> type, String location) {
    return null;
  }
}
