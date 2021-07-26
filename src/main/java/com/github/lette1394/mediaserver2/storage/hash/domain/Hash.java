package com.github.lette1394.mediaserver2.storage.hash.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Hash {
  private final String value;

  @Override
  public String toString() {
    return value;
  }
}
