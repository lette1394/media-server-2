package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import com.github.lette1394.mediaserver2.core.config.domain.CannotFindConfigException;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SingleAnnotated implements AllSingleConfigs {
  private final Deserializer loader;
  private final ClassPathFileFactory classPathFileFactory;

  @Override
  public <T> T find(Class<T> type) {
    return Option.of(type.getAnnotation(SingleFileResource.class)).toTry()
      .flatMap(classPathFileFactory::create)
      .map(path -> new FileConfig<>(type, path))
      .flatMap(loader::deserialize)
      .getOrElseThrow(throwable -> new CannotFindConfigException("""
        다음 클래스와 관련된 리소스를 찾을 수 없습니다. 다음 사항을 확인 해 주십시오.
        ===
        type: [%s]
        ===
        1. type이 SingleFileResource annotation을 가지고 있습니까?
        2. SingleFileResource annotation filePath이 유효한 값입니까?
        """.formatted(type), throwable));
  }
}
