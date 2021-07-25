package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Try;
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

    final var string = new StringReferenceResources(new ObjectMapper(new YAMLFactory()), textReference);
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
  public Try<Void> reload() {
    textReference.set(text);
    return Try.success(null);
  }
}
