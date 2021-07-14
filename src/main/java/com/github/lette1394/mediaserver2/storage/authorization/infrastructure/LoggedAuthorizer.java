package com.github.lette1394.mediaserver2.storage.authorization.infrastructure;

import com.github.lette1394.mediaserver2.storage.authorization.domain.Authorizer;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedAuthorizer implements Authorizer {
  private final Authorizer authorizer;

  @Override
  public CompletableFuture<Void> authorize() {
    return null;
  }
}
