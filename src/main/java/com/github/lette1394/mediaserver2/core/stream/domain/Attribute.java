package com.github.lette1394.mediaserver2.core.stream.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Attribute<T> {
  private final String name;
}
