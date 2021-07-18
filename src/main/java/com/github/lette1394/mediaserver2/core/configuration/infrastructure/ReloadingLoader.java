package com.github.lette1394.mediaserver2.core.configuration.infrastructure;

import com.github.lette1394.mediaserver2.core.configuration.domain.AllMultipleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.AllSingleResources;
import com.github.lette1394.mediaserver2.core.configuration.domain.Reloader;
import io.vavr.control.Option;
import java.util.concurrent.atomic.AtomicReference;

public class ReloadingLoader implements Reloader, AllSingleResources, AllMultipleResources {
  private final AtomicReference<AllSingleResources> singleRef;
  private final AtomicReference<AllMultipleResources> multiRef;

  public ReloadingLoader(
    AllSingleResources allSingleResources,
    AllMultipleResources allMultipleResources) {

    this.singleRef = new AtomicReference<>(allSingleResources);
    this.multiRef = new AtomicReference<>(allMultipleResources);
  }

  @Override
  public void reload() {

  }

  @Override
  public <T> Option<T> find(Class<T> type, String name) {
    return null;
  }

  @Override
  public <T> Option<T> find(Class<T> type) {
    return null;
  }
}

