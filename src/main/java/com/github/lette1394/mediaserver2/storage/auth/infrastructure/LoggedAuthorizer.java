package com.github.lette1394.mediaserver2.storage.auth.infrastructure;

import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedAuthorizer implements Authorizer {
  private final Authorizer authorizer;

  @Override
  public CompletionStage<Void> authorize() {
    return null;
  }
}
