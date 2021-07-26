package com.github.lette1394.mediaserver2.core.configuration.domain;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Reloader {
  CompletionStage<Void> reload();
}
