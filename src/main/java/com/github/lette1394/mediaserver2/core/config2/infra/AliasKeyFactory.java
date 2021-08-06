package com.github.lette1394.mediaserver2.core.config2.infra;

import com.github.lette1394.mediaserver2.core.config2.domain.Config;
import com.github.lette1394.mediaserver2.core.config2.domain.ConfigTypeAlias;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AliasKeyFactory implements KeyFactory {
  private final KeyFactory keyFactory;

  @Override
  public <T> Key<T> create(Class<T> type) {
    Option.of(type.getAnnotation(ConfigTypeAlias.class))
      .map(annotation -> (Class<T>)annotation.value())  // required
      .map(keyFactory::create)
      .getOrElse(() -> keyFactory.create(type));

    return null;
  }

  @Override
  public <T> Key<T> create(Class<T> type, String location) {
    return null;
  }
}
