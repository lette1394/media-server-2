package com.github.lette1394.mediaserver2.storage.auth.usecase;

import com.github.lette1394.mediaserver2.core.trace.domain.Trace;
import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import com.github.lette1394.mediaserver2.storage.auth.domain.Credential;
import com.github.lette1394.mediaserver2.storage.auth.domain.Permission;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnceInTraceAuthorizer<C extends Credential> implements Authorizer<C> {
  private final Map<Trace, C> holder;

  @Override
  public CompletionStage<C> authorize(Permission permission) {

    return null;
  }
}
