package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.config.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.config.domain.Reloader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import org.reflections.Reflections;

public class AutoReloadingResources implements Reloader {
  private final AllSingleResources single;
  private final AtomicReference<String> textReference;
  private String text;

  @Builder
  public AutoReloadingResources(String initialText, String rootScanningPackage) {
    this.text = initialText;
    this.textReference = new AtomicReference<>(initialText);

    final var objectMapper = new ObjectMapper(new YAMLFactory());
    final var string = new StringReferenceResources(objectMapper, textReference);
    final var mapped = new SingleMapped(string, new ScanningCached(new Reflections(rootScanningPackage)));
    final var autoReloading = new SingleAutoReloading(mapped);
    this.single = autoReloading;
  }

  public AllSingleResources single() {
    return single;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public CompletionStage<? super Void> reload() {
    textReference.set(text);
    return CompletableFuture.completedStage(null);
  }
}
