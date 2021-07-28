package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllMultipleConfigs;
import io.vavr.control.Try;
import java.util.Set;

final class MultiWarmingUp implements AllMultipleConfigs {
  private final AllMultipleConfigs resources;

  private MultiWarmingUp(AllMultipleConfigs resources) {
    this.resources = resources;
  }

  public static Try<AllMultipleConfigs> create(
    AllMultipleConfigs resources,
    ResourceScanner scanner,
    Warmer warmer) {

    return scanner.scan()
      .flatMap(fileResources -> warmUp(resources, fileResources, warmer))
      .map(__ -> new MultiWarmingUp(resources));
  }

  private static Try<Void> warmUp(
    AllMultipleConfigs resources,
    Set<? extends FileResource<?>> fileResources,
    Warmer warmer) {

    return warmer.submit(() -> fileResources
      .parallelStream()
      .forEach(fileResource -> resources.find(fileResource.type(), fileResource.filename())));
  }

  @Override
  public <T> T find(Class<T> type, String name) {
    return resources.find(type, name);
  }
}

