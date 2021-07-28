package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import java.net.URL;

public final class GetRequest {
  private final URL url;
  private final Headers headers;

  public GetRequest(URL url, Headers headers) {
    this.url = url;
    this.headers = headers;
  }

  public GetRequest(URL url) {
    this(url, EmptyHeaders.INSTANCE);
  }

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
