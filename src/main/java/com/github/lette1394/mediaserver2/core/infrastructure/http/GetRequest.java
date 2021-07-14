package com.github.lette1394.mediaserver2.core.infrastructure.http;

import java.net.URL;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetRequest {
  private final URL url;
  private final Headers headers;

  public String url() {
    return url.toString();
  }

  public Headers headers() {
    return headers;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
