package com.github.lette1394.mediaserver2.storage.auth.infrastructure;

import static com.github.lette1394.mediaserver2.core.fluency.domain.FluentCompletionStage.start;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.github.lette1394.mediaserver2.core.stream.domain.Payload;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.http.EmptyHeaders;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.http.GetRequest;
import com.github.lette1394.mediaserver2.core.stream.infrastructure.http.HttpClient;
import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import com.github.lette1394.mediaserver2.storage.auth.domain.Credential;
import com.github.lette1394.mediaserver2.storage.auth.domain.Permission;
import io.vavr.control.Try;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;

@RequiredArgsConstructor
final class HttpAuthorizer<P extends Payload, C extends Credential> implements Authorizer<C> {
  private final HttpClient<P> httpClient;
  private final String authEndpoint;

  @Override
  public CompletionStage<C> authorize(Permission permission) {
    start()
      .thenCompose(__ -> getRequest())
      .thenCompose(httpClient::get);
//      .thenCompose(response -> response.headers())

    return null;
  }

  private CompletableFuture<GetRequest> getRequest() {
    return Try
      .of(() -> new URIBuilder(authEndpoint, UTF_8).build())
      .mapTry(URI::toURL)
      .map(url -> new GetRequest(url, EmptyHeaders.INSTANCE))
      .toCompletableFuture();
  }
}
