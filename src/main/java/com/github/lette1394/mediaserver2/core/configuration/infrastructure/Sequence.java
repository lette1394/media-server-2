package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toList;

import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class Sequence implements Reloader {
  private final List<Reloader> reloaders;

  @Override
  @SuppressWarnings("SuspiciousToArrayCall")
  public CompletionStage<? super Void> reload() {
    final var reloads = reloaders
      .stream()
      .map(Reloader::reload)
      .collect(toList());

    return CompletableFuture.allOf(reloads.toArray(CompletableFuture[]::new));
  }
}
