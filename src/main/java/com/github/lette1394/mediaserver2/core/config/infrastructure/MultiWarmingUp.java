package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultiConfigs;
import io.vavr.control.Try;
import java.util.Set;

final class MultiWarmingUp implements AllMultiConfigs {
  private final AllMultiConfigs resources;

  private MultiWarmingUp(AllMultiConfigs resources) {
    this.resources = resources;
  }

  public static Try<AllMultiConfigs> createMultiWarmingUp(
    AllMultiConfigs resources,
    ResourceScanner scanner,
    Warmer warmer) {

    return scanner.scan()
      .flatMap(fileResources -> warmUp(resources, fileResources, warmer))
      .map(__ -> new MultiWarmingUp(resources));
  }

  private static Try<Void> warmUp(
    AllMultiConfigs resources,
    Set<? extends FileConfig<?>> fileResources,
    Warmer warmer) {

    return warmer.submit(() -> fileResources
      .parallelStream()
      .forEach(fileResource -> resources.find(fileResource.deserializedType(), fileResource.filename())));
  }

  @Override
  public <T> T find(Class<T> type, String name) {
    return resources.find(type, name);
  }
}

