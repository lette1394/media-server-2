package com.github.lette1394.mediaserver2.storage.auth.infrastructure;

import com.github.lette1394.mediaserver2.storage.auth.domain.Authorizer;
import java.util.concurrent.CompletionStage;

public class HttpAuthorizer implements Authorizer {

  @Override
  public CompletionStage<Void> authorize() {
    // 이건 로깅을 어떻게 하나?
    return null;
  }
}
