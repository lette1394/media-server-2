package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import io.vavr.control.Option;
import java.util.Map;

public interface Headers {
  Option<String> value(String name);

  Map<String, String> toMap();
}
