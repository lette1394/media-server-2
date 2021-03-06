package com.github.lette1394.mediaserver2.storage.auth.infrastructure;

import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import com.github.lette1394.mediaserver2.storage.auth.domain.Credential;
import com.github.lette1394.mediaserver2.storage.auth.domain.Permission;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoggedAuthorizer<C extends Credential> implements Authorizer<C> {
  private final Authorizer<C> authorizer;

  @Override
  public CompletionStage<C> authorize(Permission permission) {
    return null;
  }
}
