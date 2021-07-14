package com.github.lette1394.mediaserver2.storage.authorization.infrastructure;

import com.github.lette1394.mediaserver2.storage.authorization.domain.Authorizer;
import java.util.concurrent.CompletableFuture;

public class HttpAuthorizer implements Authorizer {

  @Override
  public CompletableFuture<Void> authorize() {
    // 이건 로깅을 어떻게 하나?
    return null;
  }
}
