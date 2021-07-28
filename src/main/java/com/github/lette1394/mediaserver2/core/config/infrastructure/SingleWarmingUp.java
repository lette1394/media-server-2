package com.github.lette1394.mediaserver2.core.config.infrastructure;

import com.github.lette1394.mediaserver2.core.config.domain.AllSingleConfigs;
import io.vavr.control.Try;
import java.util.Set;

final class SingleWarmingUp implements AllSingleConfigs {
  private final AllSingleConfigs allSingleConfigs;

  private SingleWarmingUp(AllSingleConfigs allSingleConfigs) {
    this.allSingleConfigs = allSingleConfigs;
  }

  static Try<AllSingleConfigs> create(
    AllSingleConfigs resources,
    ResourceScanner scanner,
    Warmer warmer) {

    return scanner.scan()
      .flatMap(fileResources -> warmUp(resources, fileResources, warmer))
      .map(__ -> new SingleWarmingUp(resources));
  }

  private static Try<Void> warmUp(
    AllSingleConfigs resources,
    Set<? extends FileResource<?>> fileResources,
    Warmer warmer) {

    return warmer.submit(() -> fileResources
      .parallelStream()
      .forEach(fileResource -> resources.find(fileResource.type())));
  }

  @Override
  public <T> T find(Class<T> type) {
    return allSingleConfigs.find(type);
  }
}

