package com.github.lette1394.mediaserver2.core.config.domain;

public interface Resource {
  // non-null
  byte[] contents();

  // yaml -> contents가 yaml 문법에 맞아야 함
  // json -> contents가 json 문법에 맞아야 함
  // text -> 제약 없음
  Type type();
}
