package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import io.vavr.control.Option;
import java.util.Collections;
import java.util.Map;

public enum EmptyHeaders implements Headers {
  INSTANCE;

  @Override
  public Option<String> value(String name) {
    return Option.none();
  }

  @Override
  public Map<String, String> toMap() {
    return Collections.emptyMap();
  }

  @Override
  public String toString() {
    return "EmptyHeaders";
  }
}
