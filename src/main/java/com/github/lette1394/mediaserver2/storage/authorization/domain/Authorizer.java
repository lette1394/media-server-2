package com.github.lette1394.mediaserver2.storage.authorization.domain;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface Authorizer {
  CompletableFuture<Void> authorize();
}
