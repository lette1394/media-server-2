package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import static java.util.stream.Collectors.toList;

import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
import java.util.List;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Sequence implements Reloader {
  private final List<Reloader> reloaders;

  @Override
  public CompletionStage<Void> reload() {
    final var reloads = reloaders
      .stream()
      .map(Reloader::reload)
      .collect(toList());

    return Try
      .sequence(reloads)
      .map(__ -> null);
  }
}
