package com.github.lette1394.mediaserver2.core.infrastructure.http;

import com.github.lette1394.mediaserver2.core.domain.Payload;
import java.util.concurrent.CompletionStage;

public interface HttpClient<P extends Payload> {
  CompletionStage<GetResponse<P>> get(GetRequest getRequest);
}
