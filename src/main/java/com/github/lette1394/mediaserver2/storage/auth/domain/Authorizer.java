package com.github.lette1394.mediaserver2.storage.auth.domain;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Authorizer {
  CompletionStage<Void> authorize();
}
