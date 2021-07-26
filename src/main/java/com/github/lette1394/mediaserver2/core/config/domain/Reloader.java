package com.github.lette1394.mediaserver2.core.config.domain;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface Reloader {
  CompletionStage<? super Void> reload();
}
