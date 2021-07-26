package com.github.lette1394.mediaserver2.core.stream.infrastructure.http;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import java.util.concurrent.CompletionStage;

public interface HttpClient<P extends Payload> {
  CompletionStage<HttpResponse<P>> get(GetRequest getRequest);
}
